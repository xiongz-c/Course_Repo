import java.awt.*;
import java.util.List;
import java.util.LinkedList;

public class GreenBall extends Ball {
    private List<ObserverBall> observers = new LinkedList<>( );
    private MainPanel subject;
    public GreenBall( int xSpeed , int ySpeed , int ballSize , MainPanel subject ) {
        super( Color.GREEN , xSpeed , ySpeed , ballSize , subject );
        this.subject = subject;
    }

    public void registerObserver( ObserverBall observerBall ) {
        this.observers.add( observerBall );
//        System.out.println(this.observers.size());
    }

    public void removeObserver( ObserverBall observerBall ) {
        this.observers.remove( observerBall );
    }

    public synchronized void notifyObservers() {
        this.observers.forEach( ( o ) -> {
            o.update( super.getX( ) , super.getY( ) );
        } );
        this.observers.removeIf( ObserverBall::isUpdate );
    }

    public void move() {
        super.move();
        if (this.subject.isStart()) {
            this.notifyObservers();
        }

    }

    @Override
    public void update( char keyChar ) {
        switch (keyChar) {
            case 'a':
                super.setXSpeed( Math.abs( super.getXSpeed( ) ) * -1 );
                break;
            case 'd':
                super.setXSpeed( Math.abs( super.getXSpeed( ) ) );
                break;
            case 's':
                super.setYSpeed( Math.abs( super.getYSpeed( ) ) );
                break;
            case 'w':
                super.setYSpeed( Math.abs( super.getYSpeed( ) ) * -1 );
        }
    }

}
