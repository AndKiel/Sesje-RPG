package rpgApp.exeptions

class ValidationException extends RuntimeException {
    ValidationException(String message){
        super(message)
    }
}
