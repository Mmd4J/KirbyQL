package me.gameisntover.kirbyql.exception;

public class SQLFunctionException extends RuntimeException{
     public SQLFunctionException(String errorMessage){
          super(errorMessage);
     }
}
