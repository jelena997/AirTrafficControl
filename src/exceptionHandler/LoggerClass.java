package exceptionHandler;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
 
public class LoggerClass
{
    private String className;
    private Handler handler;
    public LoggerClass(String className)
    {
        this.className=className;
        handler=new ConsoleHandler();
        Logger.getLogger(className).addHandler(handler);
    }
 
    public void log(Level level,Exception ex)
    {
        Logger logger = Logger.getLogger(className);
        logger.log(level, ex.fillInStackTrace().toString());
    }
}


