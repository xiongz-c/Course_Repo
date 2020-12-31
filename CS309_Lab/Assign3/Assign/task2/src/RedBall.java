import java.awt.*;

public class RedBall extends Ball implements ObserverBall{
    public RedBall(  int xSpeed , int ySpeed , int ballSize , MainPanel subject) {
        super( Color.RED , xSpeed , ySpeed , ballSize, subject );
    }
    public RedBall(  int xSpeed , int ySpeed , int ballSize , MainPanel subject, GreenBall subject2) {
        super( Color.RED , xSpeed , ySpeed , ballSize, subject );
        subject2.registerObserver( this );
    }

    @Override
    public void update( char keyChar ) {
        if (keyChar == 'a' || keyChar == 'd') {
            int temp = this.getXSpeed();
            this.setXSpeed(this.getYSpeed());
            this.setYSpeed(temp);
        }
    }

    @Override
    public void update(int x, int y) {
        int diffX = this.getX() - x;
        int diffY = this.getY() - y;
        if (Math.sqrt((double)(diffX * diffX + diffY * diffY)) <= 70.0D) {
            this.setX(diffX > 0 ? this.getX() + 40 : this.getX() - 40);
            this.setY(diffY > 0 ? this.getY() + 40 : this.getY() - 40);
            this.setColor(new Color(255, 102, 102));
            this.isUpdated = true;
        }

    }

    @Override
    public boolean isUpdate() {
        return this.isUpdated;
    }
}
