package mukul.restaurant.exception;

public class FoodNotFound extends RuntimeException {
    public FoodNotFound(String message) {
        super(message);
    }
}
