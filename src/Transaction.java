import java.time.LocalDate;

public class Transaction
{
    private final int id;
    private final String description;
    private final double amount;
    private final TransactionType type;
    private final String category;
    private final LocalDate date;
    private final boolean recurring;


    public Transaction(
        int id,
        String description,
        double amount,
        TransactionType type,
        String category,
        LocalDate date,
        boolean recurring)
    {
        if (id <= 0)
        {
            throw new IllegalArgumentException(
                "Transaction ID must be positive.");
        }

        if (!InputValidator.isValidText(description))
        {
            throw new IllegalArgumentException(
                "Description cannot be blank.");
        }

        if (!InputValidator.isValidAmount(amount))
        {
            throw new IllegalArgumentException(
                "Invalid transaction amount.");
        }

        if (type == null)
        {
            throw new IllegalArgumentException(
                "Transaction type cannot be null.");
        }

        if (!InputValidator.isValidCategory(category, type))
        {
            throw new IllegalArgumentException(
                "Invalid category for transaction type.");
        }

        if (date == null)
        {
            throw new IllegalArgumentException(
                "Date cannot be null.");
        }

        this.id = id;
        this.description = description.trim();
        this.amount = amount;
        this.type = type;
        this.category = category.trim();
        this.date = date;
        this.recurring = recurring;
    }


    public int getId()
    {
        return id;
    }


    public String getDescription()
    {
        return description;
    }


    public double getAmount()
    {
        return amount;
    }


    public TransactionType getType()
    {
        return type;
    }


    public String getCategory()
    {
        return category;
    }


    public LocalDate getDate()
    {
        return date;
    }


    public boolean isRecurring()
    {
        return recurring;
    }
}