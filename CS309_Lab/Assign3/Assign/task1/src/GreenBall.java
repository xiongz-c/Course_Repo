import java.awt.*;

public class GreenBall extends Ball {
    public GreenBall( int xSpeed , int ySpeed , int ballSize ) {
        super( Color.GREEN , xSpeed , ySpeed , ballSize );
    }
    public GreenBall(  int xSpeed , int ySpeed , int ballSize , MainPanel subject) {
        super( Color.GREEN , xSpeed , ySpeed , ballSize );
        subject.registerObserver( this );
    }


    @Override
    public void update( char keyChar ) {
        switch(keyChar) {
            case 'a':
                super.setXSpeed(Math.abs(super.getXSpeed()) * -1);
                break;
            case 'd':
                super.setXSpeed(Math.abs(super.getXSpeed()));
                break;
            case 's':
                super.setYSpeed(Math.abs(super.getYSpeed()));
                break;
            case 'w':
                super.setYSpeed(Math.abs(super.getYSpeed()) * -1);
        }
    }

}
