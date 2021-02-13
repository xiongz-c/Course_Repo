/* USER CODE BEGIN Header */
/**
 ******************************************************************************
 * @file           : main.c
 * @brief          : Main program body
 ******************************************************************************
 * @attention
 *
 * <h2><center>&copy; Copyright (c) 2020 STMicroelectronics.
 * All rights reserved.</center></h2>
 *
 * This software component is licensed by ST under BSD 3-Clause license,
 * the "License"; You may not use this file except in compliance with the
 * License. You may obtain a copy of the License at:
 *                        opensource.org/licenses/BSD-3-Clause
 *
 ******************************************************************************
 */
/* USER CODE END Header */

/* Includes ------------------------------------------------------------------*/
#include "main.h"
#include "spi.h"
#include "usart.h"
#include "gpio.h"

/* Private includes ----------------------------------------------------------*/
/* USER CODE BEGIN Includes */
#include "24l01.h"
#include "string.h"
#include "stdio.h"
#include "lcd.h"
/* USER CODE END Includes */

/* Private typedef -----------------------------------------------------------*/
/* USER CODE BEGIN PTD */

/* USER CODE END PTD */

/* Private define ------------------------------------------------------------*/
/* USER CODE BEGIN PD */
/* USER CODE END PD */

/* Private macro -------------------------------------------------------------*/
/* USER CODE BEGIN PM */

/* USER CODE END PM */

/* Private variables ---------------------------------------------------------*/

/* USER CODE BEGIN PV */
const char HEARTBEAT[] = "$BEAT";

int channel = 0;
uint8_t set_channel = 0;
uint8_t welcome = 0;
uint8_t flash = 0;
uint8_t heart_cnt = 0;
uint8_t led_cnt = 0;
uint8_t connect_status = 0;
uint8_t send_signal = 0;
uint8_t rxBuffer[10];
uint8_t rx_len = 0;
char buffer_msg[1024];
uint8_t buffer_rec[33];
char tmp[1024];
uint8_t isBegin = 0;
int last_led = -1;

static unsigned char outData_send[144] = { 0 };
static unsigned char outData_receive[144] = { 0 };
/* USER CODE END PV */

/* Private function prototypes -----------------------------------------------*/
void SystemClock_Config(void);

void painting_send(const uint8_t *content);
void painting_receive(const uint8_t *content);
/* USER CODE BEGIN PFP */
void sendUARTMessage(char *str) {
	HAL_UART_Transmit(&huart1, (uint8_t*) str, strlen(str), 0xffff);
}
void showString(uint16_t x, uint16_t y, uint16_t size, char *msg) {
	uint16_t len = strlen(msg);
	LCD_ShowString(x - len * size / 4, y - size / 2, 10000, 10000, size,
			(uint8_t*) msg);
}
/* USER CODE END PFP */

/* Private user code ---------------------------------------------------------*/
/* USER CODE BEGIN 0 */

/* USER CODE END 0 */

/**
 * @brief  The application entry point.
 * @retval int
 */
int main(void) {
	/* USER CODE BEGIN 1 */

	/* USER CODE END 1 */

	/* MCU Configuration--------------------------------------------------------*/

	/* Reset of all peripherals, Initializes the Flash interface and the Systick. */
	HAL_Init();

	/* USER CODE BEGIN Init */
	NRF24L01_Init();
	LCD_Init();
	/* USER CODE END Init */

	/* Configure the system clock */
	SystemClock_Config();

	/* USER CODE BEGIN SysInit */

	/* USER CODE END SysInit */

	/* Initialize all configured peripherals */
	MX_GPIO_Init();
	MX_SPI1_Init();
	MX_USART1_UART_Init();
	/* USER CODE BEGIN 2 */
	HAL_UART_Receive_IT(&huart1, (uint8_t*) rxBuffer, 1);

	// Initialize the vars
	// reset: return to init state
	HAL_GPIO_TogglePin(LED1_GPIO_Port, LED1_Pin);
	HAL_GPIO_TogglePin(LED0_GPIO_Port, LED0_Pin);
	memset(buffer_msg, 0, sizeof(buffer_msg));
	memset(buffer_rec, 0, sizeof(buffer_rec));
	send_signal = 0;

	LCD_Clear(WHITE);
	POINT_COLOR = BLUE;
	showString(120, 80, 24, "Wechat 2077");
	POINT_COLOR = GREEN;
	showString(120, 240, 24, "Channel Not Set");
	POINT_COLOR = BLACK;
	showString(120, 140, 24, "Welcome");
	LCD_DrawRectangle(20, 20, 220, 280);
	/* USER CODE END 2 */

	/* Infinite loop */
	/* USER CODE BEGIN WHILE */
	while (1) {
		// Check module status
		if (NRF24L01_Check()) {
			LCD_Clear(WHITE);
			set_channel = 0;
			POINT_COLOR = BLACK;
			sendUARTMessage("2.4GHz NOT FOUND\r\n");
			showString(120, 140, 24, "2.4GHz NOT FOUND");
		}
		while (NRF24L01_Check()) {
			HAL_Delay(500);
			if(last_led==0){
				HAL_GPIO_TogglePin(LED1_GPIO_Port, LED1_Pin);
				HAL_GPIO_TogglePin(LED0_GPIO_Port, LED0_Pin);
				last_led = 1;
			}else{
				HAL_GPIO_TogglePin(LED0_GPIO_Port, LED0_Pin);
				last_led = -last_led;
			}

			flash = 1;
		}

		// Check channel status
		if (set_channel == 0) {
			// Re-initialize leds
			if(last_led==1){
				HAL_GPIO_TogglePin(LED0_GPIO_Port, LED0_Pin);
				last_led=-1;
			}
			if(last_led==0){
				HAL_GPIO_TogglePin(LED1_GPIO_Port, LED1_Pin);
				last_led=-1;
			}
			last_led = -1;
			// Re-initialize vars
			memset(buffer_msg, 0, sizeof(buffer_msg));
			memset(buffer_rec, 0, sizeof(buffer_rec));
			send_signal = 0;
			welcome = 1;
			// Draw Intro Page
			if (isBegin) {
				LCD_Clear(WHITE);
				POINT_COLOR = BLUE;
				showString(120, 80, 24, "CLOSED");
				POINT_COLOR = GREEN;
				showString(120, 220, 24, "CH Clear");
				showString(120, 260, 24, "reset to restart");
				POINT_COLOR = BLACK;
				LCD_DrawRectangle(20, 20, 220, 280);
				sendUARTMessage("SET CHANNEL:");
			}

		}
		while (set_channel == 0 ) {
		}
		if (welcome) {
			showString(120, 140, 24, "  Enter: 3  ");
			HAL_Delay(500);
			showString(120, 140, 24, "  Enter: 2  ");
			HAL_Delay(500);
			showString(120, 140, 24, "  Enter: 1  ");
			HAL_Delay(200);
			welcome = 0;
			flash = 1;
			connect_status = 2;

			LCD_Clear(WHITE);
			POINT_COLOR = BLUE;
			LCD_DrawRectangle(18, 58, 212, 132);
			POINT_COLOR = BLACK;
			LCD_DrawRectangle(18, 178, 212, 252);
		}

		// Draw fundamental elements
		if (flash == 1) {
			sprintf(tmp, "CH:%d", channel);
			LCD_ShowString(10, 12, 200, 200, 16, (uint8_t*) tmp);
			if (connect_status == 0) {
				POINT_COLOR = BLACK;
				LCD_ShowString(200, 12, 100, 100, 16, (uint8_t*) "^w^");
				showString(120, 300, 16, "     CONNECT    ");
				if(last_led == 1){
					HAL_GPIO_TogglePin(LED0_GPIO_Port, LED0_Pin);
					HAL_GPIO_TogglePin(LED1_GPIO_Port, LED1_Pin);
				}
				if(last_led==-1){
					HAL_GPIO_TogglePin(LED1_GPIO_Port, LED1_Pin);
				}
				last_led = 0;
			} else {
				POINT_COLOR = RED;
				LCD_ShowString(200, 12, 100, 100, 16, (uint8_t*) "QwQ");
				showString(120, 300, 16, "CONNECTION LOST");

				sprintf(tmp, "CH:%d", last_led);
				sendUARTMessage(tmp);
				if(last_led == 0){
					HAL_GPIO_TogglePin(LED0_GPIO_Port, LED0_Pin);
					HAL_GPIO_TogglePin(LED1_GPIO_Port, LED1_Pin);
				}
				if(last_led==-1){
					HAL_GPIO_TogglePin(LED0_GPIO_Port, LED0_Pin);
				}
				last_led = 1;
			}

			POINT_COLOR = BLACK;
			LCD_DrawLine(10, 30, 230, 30);
			LCD_DrawLine(10, 290, 230, 290);

			flash = 0;
		}

		// Handle RX messages
		NRF24L01_RX_Mode(channel);
		memset(buffer_rec, 0, sizeof(buffer_rec));
		if (NRF24L01_RxPacket(buffer_rec) == 0) {
			if (strcmp((char*) buffer_rec, HEARTBEAT) == 0)
				continue;
			uint8_t rec_len = 0;

			while (buffer_rec[rec_len] != '\0')
				rec_len++;

			if (rec_len != 0) {
				painting_receive(buffer_rec);
				flash |= 1;
			}
		}

		// Handle TX messages
		if (send_signal == 1 && strlen(buffer_msg) != 0) {
			NRF24L01_TX_Mode(channel);

			uint8_t tmp_status = (connect_status == 2) ? 2 : 1;
			if (NRF24L01_TxPacket((uint8_t*) buffer_msg) == TX_OK) {
				tmp_status = 0;
			}
			connect_status = tmp_status;

			if (tmp_status == 0) {
				painting_send((uint8_t*) buffer_msg);
			}

			send_signal = 0;
			flash |= 1;
		}

		// Connection test (Heart Packet)
		heart_cnt++;
		if ( heart_cnt == 10 ) {
			NRF24L01_TX_Mode(channel);
			uint8_t temp_status;
			if(NRF24L01_TxPacket((uint8_t*) HEARTBEAT) == TX_OK){
				temp_status = 0;
			}else{
				if(connect_status == 2){
					temp_status = 2;
				}else{
					temp_status = 1;
				}
			}
			if (connect_status == temp_status) {
//				flash |= 0;
			} else {
				flash = 1;
			}

			connect_status = temp_status;
			heart_cnt = 0;
		}

		// LED setting
//		if (connect_status == 0) {
//			HAL_GPIO_WritePin(LED1_GPIO_Port, LED1_Pin, GPIO_PIN_RESET);
//			HAL_GPIO_WritePin(LED0_GPIO_Port, LED0_Pin, GPIO_PIN_SET);
//		} else if (connect_status == 1) {
//			led_cnt++;
//			if (led_cnt == LED_FLASH) {
//				HAL_GPIO_TogglePin(LED0_GPIO_Port, LED0_Pin);
//				led_cnt = 0;
//			}
//			HAL_GPIO_WritePin(LED1_GPIO_Port, LED1_Pin, GPIO_PIN_SET);
//		} else {
//			HAL_GPIO_WritePin(LED0_GPIO_Port, LED0_Pin, GPIO_PIN_RESET);
//			HAL_GPIO_WritePin(LED1_GPIO_Port, LED1_Pin, GPIO_PIN_SET);
//		}

		// Delay for next round
		HAL_Delay(100);

		/* USER CODE END WHILE */

		/* USER CODE BEGIN 3 */
	}
	/* USER CODE END 3 */
}

/**
 * @brief System Clock Configuration
 * @retval None
 */
void SystemClock_Config(void) {
	RCC_OscInitTypeDef RCC_OscInitStruct = { 0 };
	RCC_ClkInitTypeDef RCC_ClkInitStruct = { 0 };

	/** Initializes the CPU, AHB and APB busses clocks
	 */
	RCC_OscInitStruct.OscillatorType = RCC_OSCILLATORTYPE_HSE;
	RCC_OscInitStruct.HSEState = RCC_HSE_ON;
	RCC_OscInitStruct.HSEPredivValue = RCC_HSE_PREDIV_DIV1;
	RCC_OscInitStruct.HSIState = RCC_HSI_ON;
	RCC_OscInitStruct.PLL.PLLState = RCC_PLL_ON;
	RCC_OscInitStruct.PLL.PLLSource = RCC_PLLSOURCE_HSE;
	RCC_OscInitStruct.PLL.PLLMUL = RCC_PLL_MUL9;
	if (HAL_RCC_OscConfig(&RCC_OscInitStruct) != HAL_OK) {
		Error_Handler();
	}
	/** Initializes the CPU, AHB and APB busses clocks
	 */
	RCC_ClkInitStruct.ClockType = RCC_CLOCKTYPE_HCLK | RCC_CLOCKTYPE_SYSCLK
			| RCC_CLOCKTYPE_PCLK1 | RCC_CLOCKTYPE_PCLK2;
	RCC_ClkInitStruct.SYSCLKSource = RCC_SYSCLKSOURCE_PLLCLK;
	RCC_ClkInitStruct.AHBCLKDivider = RCC_SYSCLK_DIV1;
	RCC_ClkInitStruct.APB1CLKDivider = RCC_HCLK_DIV2;
	RCC_ClkInitStruct.APB2CLKDivider = RCC_HCLK_DIV1;

	if (HAL_RCC_ClockConfig(&RCC_ClkInitStruct, FLASH_LATENCY_2) != HAL_OK) {
		Error_Handler();
	}
}

/* USER CODE BEGIN 4 */
void painting_send(const uint8_t *content) {
	static unsigned char uRx_Data[48] = { 0 };
	static unsigned char uLength = 0;
	static unsigned char l = 0;

	for (int p = 0; p < 33; p++) {
		if (content[p] == '\0') {
			break;
		}

		if (uLength >= 48) {
			for (int i = 0; i < 48; i++) {
				uRx_Data[i] = uRx_Data[i + 16];
			}
			for (int i = 32; i < 48; i++) {
				uRx_Data[i] = 0;
			}
			uLength = 32;
		}
		uRx_Data[uLength] = content[p];
		uLength++;
	}

	l = uLength;
	while ((l) % 16 != 0) {
		l++;
	}
	int blank = l - uLength;
	int first = 16 - blank;
	for (int i = l - 1; i >= l - first; i--) {
		uRx_Data[i] = uRx_Data[i - blank];
	}
	for (int i = l - 16; i < l - first; i++) {
		uRx_Data[i] = ' ';
	}
	memcpy(outData_send, uRx_Data, 48);

	while (uLength % 16 > 0) {
		uLength++;
	}

	POINT_COLOR = BLUE;
	LCD_ShowString(20, 60, 192, 72, 24, (uint8_t*) outData_send);
}

void painting_receive(const uint8_t *content) {
	static unsigned char uRx_Data[48] = { 0 };
	static unsigned char uLength = 0;

	for (int p = 0; p < 33; p++) {
		if (content[p] == '\0') {
			break;
		}

		if (uLength >= 48) {
			for (int i = 0; i < 32; i++) {
				uRx_Data[i] = uRx_Data[i + 16];
			}
			for (int i = 32; i < 48; i++) {
				uRx_Data[i] = 0;
			}
			uLength = 32;
		}
		uRx_Data[uLength] = content[p];
		uLength++;
	}

	while (uLength % 16 > 0) {
		uRx_Data[uLength] = ' ';
		uLength++;
	}

	memcpy(outData_receive, uRx_Data, 48);

	POINT_COLOR = BLACK;
	LCD_ShowString(20, 180, 192, 72, 24, (uint8_t*) outData_receive);
}

void HAL_UART_RxCpltCallback(UART_HandleTypeDef *huart) {
	if (huart->Instance == USART1) {
		if (rxBuffer[0] == '\n') {
			if (strcmp(buffer_msg, "$OPEN") == 0 && (set_channel == 0)) {
				if (set_channel == 0) {
					channel = 80;
					set_channel = 1;
					sprintf(tmp, "SET CHANNEL AS %d SUCCESSFULLY.\n",
					80);
					sendUARTMessage(tmp);
				}
			} else if (strcmp(buffer_msg, "$CLOSE") == 0) {
				set_channel = 0;
				sendUARTMessage("CLOSE CONNECTION SUCCESSFULLY.\n");
			} else if (set_channel == 0) {
				set_channel = 1;
				channel = 0;
				for (int i = 0; i < rx_len; i++) {
					channel *= 10;
					channel += (int) (buffer_msg[i] - '0');
				}
				sprintf(tmp, "SET CHANNEL AS %d SUCCESSFULLY.\n", channel);
				sendUARTMessage(tmp);
			} else {
				send_signal = 1;
				buffer_msg[rx_len] = '\0';
			}
			rx_len = 0;
		} else {
			buffer_msg[rx_len] = rxBuffer[0];
			rx_len++;
		}
	}
}

void HAL_GPIO_EXTI_Callback(uint16_t GPIO_Pin) {
	switch (GPIO_Pin) {
	case KEY0_Pin:
		if (set_channel == 0) {
			channel = 80;
			set_channel = 1;
		}
		isBegin = 1;
		break;
	case KEY1_Pin:
		set_channel = 0;
		break;
	default:
		break;
	}
	HAL_Delay(100);
}
/* USER CODE END 4 */

/**
 * @brief  This function is executed in case of error occurrence.
 * @retval None
 */
void Error_Handler(void) {
	/* USER CODE BEGIN Error_Handler_Debug */
	/* User can add his own implementation to report the HAL error return state */

	/* USER CODE END Error_Handler_Debug */
}

#ifdef  USE_FULL_ASSERT
/**
  * @brief  Reports the name of the source file and the source line number
  *         where the assert_param error has occurred.
  * @param  file: pointer to the source file name
  * @param  line: assert_param error line source number
  * @retval None
  */
void assert_failed(uint8_t *file, uint32_t line)
{
  /* USER CODE BEGIN 6 */
  /* User can add his own implementation to report the file name and line number,
     tex: printf("Wrong parameters value: file %s on line %d\r\n", file, line) */
  /* USER CODE END 6 */
}
#endif /* USE_FULL_ASSERT */

/************************ (C) COPYRIGHT STMicroelectronics *****END OF FILE****/
