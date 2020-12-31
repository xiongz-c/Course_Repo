import java.awt.*;

public class RedBall extends Ball {
    public RedBall(  int xSpeed , int ySpeed , int ballSize ) {
        super( Color.RED , xSpeed , ySpeed , ballSize );
    }
    public RedBall(  int xSpeed , int ySpeed , int ballSize , MainPanel subject) {
        super( Color.RED , xSpeed , ySpeed , ballSize );
        subject.registerObserver( this );
    }

    @Override
    public void update( char keyChar ) {
        if (keyChar == 'a' || keyChar == 'd') {
            int temp = super.getXSpeed();
            super.setXSpeed(super.getYSpeed());
            super.setYSpeed(temp);
        }

    }
}
