import java.awt.*;

public class BlueBall extends Ball implements ObserverBall{

    public BlueBall( int xSpeed , int ySpeed , int ballSize , MainPanel subject ) {
        super( Color.BLUE , xSpeed , ySpeed , ballSize,subject );
    }

    public BlueBall( int xSpeed , int ySpeed , int ballSize , MainPanel subject, GreenBall subject2 ) {
        super( Color.BLUE , xSpeed , ySpeed , ballSize, subject);
        subject2.registerObserver( this );
    }
    @Override
    public void update( char keyChar ) {
        super.setXSpeed( -1 * super.getXSpeed( ) );
        super.setYSpeed( -1 * super.getYSpeed( ) );
    }

    public void update(int x, int y) {
        int diffX = this.getX() - x;
        int diffY = this.getY() - y;
        if (Math.sqrt((double)(diffX * diffX + diffY * diffY)) <= 80.0D) {
            this.setX(diffX > 0 ? this.getX() + 30 : this.getX() - 30);
            this.setY(diffY > 0 ? this.getY() + 30 : this.getY() - 30);
            this.isUpdated = true;
            this.setColor(new Color(51, 153, 255));
        }

    }

    public boolean isUpdate() {
        return this.isUpdated;
    }
}


