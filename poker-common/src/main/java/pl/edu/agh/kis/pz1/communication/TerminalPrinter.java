package pl.edu.agh.kis.pz1.communication;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class TerminalPrinter {
    private Logger logger = LogManager.getLogger(TerminalPrinter.class);

    public void print(String s){
        logger.info(s);
    }

    public void printError(Exception e){
        logger.info(e.getMessage());
    }

    public void printError(String s,Exception e){
        this.print(s);
        this.printError(e);
    }

    public void printError(Exception e, String s){
        this.printError(e);
        this.print(s);
    }
}
