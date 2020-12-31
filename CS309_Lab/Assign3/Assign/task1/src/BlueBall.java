import java.awt.*;

public class BlueBall extends Ball {
    public BlueBall( int xSpeed , int ySpeed , int ballSize ) {
        super( Color.BLUE , xSpeed , ySpeed , ballSize );
    }

    public BlueBall( int xSpeed , int ySpeed , int ballSize , MainPanel subject ) {
        super( Color.BLUE , xSpeed , ySpeed , ballSize );
        subject.registerObserver( this );
    }

    @Override
    public void update( char keyChar ) {
        super.setXSpeed( -1 * super.getXSpeed( ) );
        super.setYSpeed( -1 * super.getYSpeed( ) );
    }
}


