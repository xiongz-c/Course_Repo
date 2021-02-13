#include "24l01.h"
#include "spi.h"
//#include "delay.h"
//////////////////////////////////////////////////////////////////////////////////	 
//±¾³ÌÐòÖ»¹©Ñ§Ï°Ê¹ÓÃ£¬Î´¾­×÷ÕßÐí¿É£¬²»µÃÓÃÓÚÆäËüÈÎºÎÓÃÍ¾
//ALIENTEK STM32F103¿ª·¢°å
//NRF24L01Çý¶¯´úÂë	   
//ÕýµãÔ­×Ó@ALIENTEK
//¼¼ÊõÂÛÌ³:www.openedv.com
//´´½¨ÈÕÆÚ:2017/6/1
//°æ±¾£ºV1.0
//°æÈ¨ËùÓÐ£¬µÁ°æ±Ø¾¿¡£
//Copyright(C) ¹ãÖÝÊÐÐÇÒíµç×Ó¿Æ¼¼ÓÐÏÞ¹«Ë¾ 2014-2024
//All rights reserved									  
////////////////////////////////////////////////////////////////////////////////// 	

const uint8_t TX_ADDRESS[TX_ADR_WIDTH]={0x34,0x43,0x10,0x10,0x01}; //发送地址
const uint8_t RX_ADDRESS[RX_ADR_WIDTH]={0x34,0x43,0x10,0x10,0x01}; //接收地址

//针对 NRF24L01 修改 SPI1 驱动
void NRF24L01_SPI_Init(void)
{
    __HAL_SPI_DISABLE(&hspi1);               //先关闭 SPI1
    hspi1.Init.CLKPolarity=SPI_POLARITY_LOW; //串行同步时钟的空闲状态为低电平
    hspi1.Init.CLKPhase=SPI_PHASE_1EDGE;     //串行同步时钟的第 1 个跳变沿（上升或下降）数据被采样
    HAL_SPI_Init(&hspi1);
    __HAL_SPI_ENABLE(&hspi1);                //使能 SPI1
}

//初始化 24L01 的 IO 口
void NRF24L01_Init(void)
{
    GPIO_InitTypeDef GPIO_Initure;
    __HAL_RCC_GPIOA_CLK_ENABLE();			//开启 GPIOA 时钟
    __HAL_RCC_GPIOC_CLK_ENABLE();			//开启 GPIOC 时钟
    
	//PA2,3,4 初始化设置:推挽输出
    GPIO_Initure.Pin=GPIO_PIN_2|GPIO_PIN_3|GPIO_PIN_4; 
    GPIO_Initure.Mode=GPIO_MODE_OUTPUT_PP;  //推挽输出
    GPIO_Initure.Pull=GPIO_PULLUP;          //上拉
    GPIO_Initure.Speed=GPIO_SPEED_FREQ_HIGH;//高速
    HAL_GPIO_Init(GPIOA,&GPIO_Initure);     //初始化

	//PC4 推挽输出
    GPIO_Initure.Pin=GPIO_PIN_4;			//PC4
    HAL_GPIO_Init(GPIOC,&GPIO_Initure);     //初始化
	
	//PA1 上拉输出
	GPIO_Initure.Pin=GPIO_PIN_1;			//PA1
	GPIO_Initure.Mode=GPIO_MODE_INPUT;      //输入
	HAL_GPIO_Init(GPIOA,&GPIO_Initure);     //初始化
	
	HAL_GPIO_WritePin(GPIOA,GPIO_PIN_1,GPIO_PIN_SET);
	HAL_GPIO_WritePin(GPIOA,GPIO_PIN_2,GPIO_PIN_SET);
	HAL_GPIO_WritePin(GPIOA,GPIO_PIN_3,GPIO_PIN_SET);
	HAL_GPIO_WritePin(GPIOA,GPIO_PIN_4,GPIO_PIN_SET);
    
	MX_SPI1_Init();    		                //初始化SPI1
    NRF24L01_SPI_Init();                    //针对NRF的特点修改SPI的设置
    NRF24L01_CE_LOW(); 			                //使能24L01
    NRF24L01_CSN_HEIGH();			                //SPI片选取消
}
//检测 24L01 是否存在
//返回值:0，成功;1，失败
uint8_t NRF24L01_Check(void)
{
	uint8_t buf[5]={0XA5,0XA5,0XA5,0XA5,0XA5};
	uint8_t i;
	SPI1_SetSpeed(SPI_BAUDRATEPRESCALER_8); //spi 速度为 10.5Mhz（（24L01 的最大 SPI 时钟为 10Mhz,这里大一点没关系）
	NRF24L01_Write_Buf(NRF_WRITE_REG+TX_ADDR,buf,5);//写入 5 个字节的地址.
	NRF24L01_Read_Buf(TX_ADDR,buf,5); //读出写入的地址
	for(i=0;i<5;i++)if(buf[i]!=0XA5)break;	 							   
	if(i!=5)return 1;//检测 24L01 错误
	return 0;		 //检测到 24L01
}	 	 
//SPI写寄存器
//reg::指定寄存器地址
//value:写入的值
uint8_t NRF24L01_Write_Reg(uint8_t reg,uint8_t value)
{
	uint8_t status;
	NRF24L01_CSN_LOW();                 //使能 SPI 传输
  	status =SPI1_ReadWriteByte(reg);//发送寄存器号
  	SPI1_ReadWriteByte(value);      //写入寄存器的值
  	NRF24L01_CSN_HEIGH();                 //禁止 SPI 传输
  	return(status);       		    //返回状态值
}
//读取 SPI 寄存器值
//reg:要读的寄存器
uint8_t NRF24L01_Read_Reg(uint8_t reg)
{
	uint8_t reg_val;
	NRF24L01_CSN_LOW();             //使能 SPI 传输
  	SPI1_ReadWriteByte(reg);    //发送寄存器号
  	reg_val=SPI1_ReadWriteByte(0XFF);//读取寄存器内容
  	NRF24L01_CSN_HEIGH();             //禁止 SPI 传输
  	return(reg_val);            //返回状态值
}	
//在指定位置读出指定长度的数据
//reg:寄存器(位置)
//*pBuf:数据指针
//len:数据长度
//返回值,此次读到的状态寄存器值
uint8_t NRF24L01_Read_Buf(uint8_t reg,uint8_t *pBuf,uint8_t len)
{
	uint8_t status,uint8_t_ctr;
	NRF24L01_CSN_LOW();            //使能 SPI 传输
  	status=SPI1_ReadWriteByte(reg);//发送寄存器值(位置),并读取状态值
	for(uint8_t_ctr=0;uint8_t_ctr<len;uint8_t_ctr++)pBuf[uint8_t_ctr]=SPI1_ReadWriteByte(0XFF);//读出数据
	NRF24L01_CSN_HEIGH();            //关闭 SPI 传输
  	return status;             //返回读到的状态值
}
//在指定位置写指定长度的数据
//reg:寄存器(位置)
//*pBuf:数据指针
//len:数据长度
//返回值,此次读到的状态寄存器值
uint8_t NRF24L01_Write_Buf(uint8_t reg, uint8_t *pBuf, uint8_t len)
{
	uint8_t status,uint8_t_ctr;
	NRF24L01_CSN_LOW();            //使能 SPI 传输
  	status = SPI1_ReadWriteByte(reg);//发送寄存器值(位置),并读取状态值
  	for(uint8_t_ctr=0; uint8_t_ctr<len; uint8_t_ctr++)SPI1_ReadWriteByte(*pBuf++); //写入数据
  	NRF24L01_CSN_HEIGH();             //关闭 SPI 传输
  	return status;              //返回读到的状态值
}				   
//启动 NRF24L01 发送一次数据
//txbuf:待发送数据首地址
//返回值:发送完成状况
uint8_t NRF24L01_TxPacket(uint8_t *txbuf)
{
	uint8_t sta;
 	SPI1_SetSpeed(SPI_BAUDRATEPRESCALER_8); //spi 速度为 6.75Mhz（24L01 的最大 SPI 时钟为 10Mhz）
 	NRF24L01_CE_LOW();
  	NRF24L01_Write_Buf(WR_TX_PLOAD,txbuf,TX_PLOAD_WIDTH);//写数据到 TX BUF 32 个字节
 	NRF24L01_CE_HEIGH();                         //启动发送
	while(NRF24L01_IRQ!=0);                 //等待发送完成
	sta=NRF24L01_Read_Reg(STATUS);          //读取状态寄存器的值
	NRF24L01_Write_Reg(NRF_WRITE_REG+STATUS,sta); //清除 TX_DS 或 MAX_RT 中断标志
	if(sta&MAX_TX)                          //达到最大重发次数
	{
		NRF24L01_Write_Reg(FLUSH_TX,0xff);  //清除 TX FIFO 寄存器
		return MAX_TX; 
	}
	if(sta&TX_OK)                           //发送完成
	{
		return TX_OK;
	}
	return 0xff;//其他原因发送失败
}
//启动 NRF24L01 发送一次数据
//txbuf:待发送数据首地址
//返回值:0，接收完成；其他，错误代码
uint8_t NRF24L01_RxPacket(uint8_t *rxbuf)
{
	uint8_t sta;
	SPI1_SetSpeed(SPI_BAUDRATEPRESCALER_8); ///spi 速度为 6.75Mhz（24L01 的最大 SPI 时钟为 10Mhz）
	sta=NRF24L01_Read_Reg(STATUS);          //读取状态寄存器的值
	NRF24L01_Write_Reg(NRF_WRITE_REG+STATUS,sta); //清除 TX_DS 或 MAX_RT 中断标志
	if(sta&RX_OK)//接收到数据
	{
		NRF24L01_Read_Buf(RD_RX_PLOAD,rxbuf,RX_PLOAD_WIDTH);//读取数据
		NRF24L01_Write_Reg(FLUSH_RX,0xff);  //清除 RX FIFO 寄存器
		return 0; 
	}	   
	return 1;//没收到任何数据
}					    
//该函数初始化 NRF24L01 到 RX 模式
//设置 RX 地址,写 RX 数据宽度,选择 RF 频道,波特率和 LNA HCURR
//当 CE 变高后,即进入 RX 模式,并可以接收数据了
void NRF24L01_RX_Mode(int channel)
{
	NRF24L01_CSN_LOW();
  	NRF24L01_Write_Buf(NRF_WRITE_REG+RX_ADDR_P0,(uint8_t*)RX_ADDRESS,RX_ADR_WIDTH);//写 RX 节点地址
	  
  	NRF24L01_Write_Reg(NRF_WRITE_REG+EN_AA,0x01);       //使能通道 0 的自动应答
  	NRF24L01_Write_Reg(NRF_WRITE_REG+EN_RXADDR,0x01);   //使能通道 0 接收地址
  	NRF24L01_Write_Reg(NRF_WRITE_REG+RF_CH,channel);	        //设置 RF 通信频率
  	NRF24L01_Write_Reg(NRF_WRITE_REG+RX_PW_P0,RX_PLOAD_WIDTH);//选择通道 0 的有效数据宽度
  	NRF24L01_Write_Reg(NRF_WRITE_REG+RF_SETUP,0x0f);    //设置 TX 发射参数,0db 增益,2Mbps,低噪声增益开启
  	NRF24L01_Write_Reg(NRF_WRITE_REG+CONFIG, 0x0f);     ///配置基本工作模式的参数;PWR_UP,EN_CRC,16BIT_CRC,接收模式
  	NRF24L01_CE_HEIGH(); //CE 为高,进入接收模式
}						 
//该函数初始化 NRF24L01 到 TX 模式
//设置 TX 地址,写 TX 数据宽度,设置 RX 自动应答的地址,填充 TX 发送数据,
//选择 RF 频道,波特率和 LNA HCURR
//PWR_UP,CRC 使能
//当 CE 变高后,即进入 RX 模式,并可以接收数据了
//CE 为高大于 10us,则启动发送
void NRF24L01_TX_Mode(int channel)
{														 
	NRF24L01_CE_LOW();
	NRF24L01_Write_Buf(NRF_WRITE_REG+TX_ADDR,(uint8_t*)TX_ADDRESS,TX_ADR_WIDTH);//写 TX 节点地址
	NRF24L01_Write_Buf(NRF_WRITE_REG+RX_ADDR_P0,(uint8_t*)RX_ADDRESS,RX_ADR_WIDTH); //设置 TX 节点地址,主要为了使能 ACK

	NRF24L01_Write_Reg(NRF_WRITE_REG+EN_AA,0x01);     //使能通道 0 的自动应答
	NRF24L01_Write_Reg(NRF_WRITE_REG+EN_RXADDR,0x01); //使能通道 0 接收地址
	NRF24L01_Write_Reg(NRF_WRITE_REG+SETUP_RETR,0x1a);//设置自动重发间隔时间:500us + 86us;最大自动重发次数:10 次
	NRF24L01_Write_Reg(NRF_WRITE_REG+RF_CH,channel);       //设置 RF 通道为 40
	NRF24L01_Write_Reg(NRF_WRITE_REG+RF_SETUP,0x0f);  //设置 TX 发射参数,0db 增益,2Mbps,低噪声增益开启
	NRF24L01_Write_Reg(NRF_WRITE_REG+CONFIG,0x0e);    //配置基本工作模式的参数;PWR_UP,EN_CRC,16BIT_CRC,接收模式,开启所有中断
	NRF24L01_CE_HEIGH();//CE 为高,10us 后启动发送
}
