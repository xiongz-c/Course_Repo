import annotations.Inject;
import annotations.Value;

import java.io.*;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class BeanFactoryImpl implements BeanFactory {
    private final Properties valueProperties;
    private final Properties injectProperties;

    BeanFactoryImpl() {
        this.valueProperties = new Properties( );
        this.injectProperties = new Properties( );
    }

    @Override
    public void loadInjectProperties( File file ) {
        try {
            this.injectProperties.load( new FileInputStream( file ) );
        } catch (IOException e) {
            e.printStackTrace( );
        }
    }

    @Override
    public void loadValueProperties( File file ) {
        try {
            this.valueProperties.load( new FileInputStream( file ) );
        } catch (IOException e) {
            e.printStackTrace( );
        }

    }

    @Override
    public <T> T createInstance( Class<T> clazz ) {
        try {
            String implClazzName = this.injectProperties.getProperty( clazz.getName( ) );
            Class<?> implClazz;
            implClazz = implClazzName == null ? clazz : Class.forName( implClazzName );
            Constructor[] constructors = implClazz.getDeclaredConstructors( );
            Constructor constructor = null;
            for (Constructor c : constructors
            ) {
                if ( c.getAnnotation( Inject.class ) != null ) {
                    constructor = c;
                    break;
                }
            }
            if ( constructor == null ) {
                constructor = implClazz.getDeclaredConstructor( );
            }
            List<Object> paraObjects = new ArrayList<>( );
            for (Parameter p : constructor.getParameters( )
            ) {
                if ( p.getAnnotation( Value.class ) != null ) {
                    Object temp;
                    Class<?> clazz0 = p.getType( ) ;
                    Value value0 = p.getAnnotation( Value.class );
                    String injectValue = this.valueProperties.getProperty( value0.value( ) );
                    if ( injectValue == null ) injectValue = value0.value( );
                    if ( clazz0.isArray( ) ) {
                        String[] strings = injectValue.split( value0.delimiter( ) );
                        Object o = Array.newInstance( clazz0.getComponentType( ) , strings.length );
                        int strLen = strings.length;
                        for (int i = 0; i < strLen; i++
                        ) {
                            Array.set( o , i , Objects.requireNonNull( typeWrapper( clazz0.getComponentType( ) ) ).getDeclaredConstructor( String.class ).newInstance( strings[i] ) );
                        }
                        temp = o;
                    } else {
                        temp = Objects.requireNonNull( typeWrapper( clazz0 ) ).getDeclaredConstructor( String.class ).newInstance( injectValue );
                    }

                    paraObjects.add( temp  );
                } else {
                    paraObjects.add( createInstance( p.getType( ) ) );
                }
            }
            Object object;
            object = constructor.newInstance( paraObjects.toArray( ) );
            for (Field f : implClazz.getDeclaredFields( )
            ) {
                boolean accessible = f.canAccess( object );
                if ( f.getAnnotation( Inject.class ) != null ) {
                    f.setAccessible( true );
                    f.set( object , createInstance( f.getType( ) ) );
                    f.setAccessible( accessible );
                }
                if ( f.getAnnotation( Value.class ) != null ) {
                    f.setAccessible( true );
                    Object temp;
                    Class<?> clazz0 = f.getType( ) ;
                    Value value0 = f.getAnnotation( Value.class );
                    String injectValue = this.valueProperties.getProperty( value0.value( ) );
                    if ( injectValue == null ) injectValue = value0.value( );
                    if ( clazz0.isArray( ) ) {
                        String[] strings = injectValue.split( value0.delimiter( ) );
                        Object o = Array.newInstance( clazz0.getComponentType( ) , strings.length );
                        int strLen = strings.length;
                        for (int i = 0; i < strLen; i++
                        ) {
                            Array.set( o , i , Objects.requireNonNull( typeWrapper( clazz0.getComponentType( ) ) ).getDeclaredConstructor( String.class ).newInstance( strings[i] ) );
                        }
                        temp = o;
                    } else {
                        temp = Objects.requireNonNull( typeWrapper( clazz0 ) ).getDeclaredConstructor( String.class ).newInstance( injectValue );
                    }
                    f.set( object , temp);
                    f.setAccessible( accessible );
                }
            }
            return (T) object;
        } catch (Exception e) {
            e.printStackTrace( );
        }
        return null;
    }

    private Class<?> typeWrapper( Class<?> clazz ) {
        if ( !clazz.isPrimitive( ) ) return clazz;
        if ( Byte.TYPE.equals( clazz ) || clazz == byte.class ) return Byte.class;
        if ( Short.TYPE.equals( clazz ) || clazz == short.class  ) return Short.class;
        if ( Integer.TYPE.equals( clazz ) || clazz == int.class ) return Integer.class;
        if ( Long.TYPE.equals( clazz ) || clazz == long.class ) return Long.class;
        if ( Double.TYPE.equals( clazz ) || clazz == double.class ) return Double.class;
        if ( Float.TYPE.equals( clazz ) || clazz == float.class ) return Float.class;
        if ( Boolean.TYPE.equals( clazz ) || clazz == boolean.class ) return Boolean.class;
        if ( Void.TYPE.equals( clazz ) || clazz == void.class ) return Void.class;
        return clazz;
    }
}
