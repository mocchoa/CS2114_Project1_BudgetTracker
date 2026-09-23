import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class InputValidator
{
    private static final String[] INCOME_CATEGORIES =
    {
        "Salary",
        "Other Income"
    };

    private static final String[] EXPENSE_CATEGORIES =
    {
        "Housing",
        "Insurance",
        "Utilities",
        "Groceries",
        "Transportation",
        "Entertainment",
        "Subscriptions",
        "Fast Food",
        "Miscellaneous",
        "Emergency Fund",
        "Rainy Day"
    };


    public static boolean isValidAmount(double amount)
    {
        return amount > 0
            && amount <= 1000000
            && !Double.isNaN(amount)
            && !Double.isInfinite(amount);
    }


    public static boolean isValidText(String text)
    {
        return text != null
            && !text.trim().isEmpty();
    }


    public static boolean isValidCategory(
        String category,
        TransactionType type)
    {
        if (!isValidText(category) || type == null)
        {
            return false;
        }

        String value = category.trim();

        if (type == TransactionType.INCOME)
        {
            for (String categoryName : INCOME_CATEGORIES)
            {
                if (categoryName.equalsIgnoreCase(value))
                {
                    return true;
                }
            }

            return false;
        }

        for (String categoryName : EXPENSE_CATEGORIES)
        {
            if (categoryName.equalsIgnoreCase(value))
            {
                return true;
            }
        }

        return false;
    }


    public static boolean isValidDate(String dateText)
    {
        if (!isValidText(dateText))
        {
            return false;
        }

        String value = dateText.trim();

        if (!value.matches("\\d{4}-\\d{2}-\\d{2}"))
        {
            return false;
        }

        DateTimeFormatter formatter =
            DateTimeFormatter
                .ofPattern("uuuu-MM-dd")
                .withResolverStyle(
                    ResolverStyle.STRICT);

        try
        {
            LocalDate.parse(value, formatter);
            return true;
        }
        catch (DateTimeParseException e)
        {
            return false;
        }
    }


    public static boolean isValidTransactionId(
        int id,
        BudgetManager manager)
    {
        return id > 0
            && manager != null
            && manager.findTransaction(id) != null;
    }


    public static boolean requiresConfirmation(double amount)
    {
        return isValidAmount(amount)
            && amount >= 10000;
    }
}