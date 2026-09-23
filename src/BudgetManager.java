import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BudgetManager
{
    private ArrayList<Transaction> transactions;


    public BudgetManager()
    {
        transactions = new ArrayList<Transaction>();
    }


    public boolean addTransaction(Transaction transaction)
    {
        if (transaction == null)
        {
            return false;
        }

        if (findTransaction(transaction.getId()) != null)
        {
            return false;
        }

        transactions.add(transaction);
        return true;
    }


    public boolean removeTransaction(int id)
    {
        Transaction transaction = findTransaction(id);

        if (transaction == null)
        {
            return false;
        }

        transactions.remove(transaction);
        return true;
    }


    public boolean editTransaction(
        int id,
        Transaction replacement)
    {
        if (replacement == null)
        {
            return false;
        }

        if (replacement.getId() != id)
        {
            return false;
        }

        for (int i = 0; i < transactions.size(); i++)
        {
            if (transactions.get(i).getId() == id)
            {
                transactions.set(i, replacement);
                return true;
            }
        }

        return false;
    }


    public Transaction findTransaction(int id)
    {
        if (id <= 0)
        {
            return null;
        }

        for (Transaction transaction : transactions)
        {
            if (transaction.getId() == id)
            {
                return transaction;
            }
        }

        return null;
    }


    public double getTotal(TransactionType type)
    {
        if (type == null)
        {
            throw new IllegalArgumentException(
                "Transaction type cannot be null.");
        }

        double total = 0.0;

        for (Transaction transaction : transactions)
        {
            if (transaction.getType() == type)
            {
                total += transaction.getAmount();
            }
        }

        return total;
    }


    public double getBalance()
    {
        return getTotal(TransactionType.INCOME)
            - getTotal(TransactionType.EXPENSE);
    }


    public double getCategoryTotal(String category)
    {
        if (category == null)
        {
            return 0.0;
        }

        double total = 0.0;

        for (Transaction transaction : transactions)
        {
            if (transaction.getCategory()
                .equalsIgnoreCase(category.trim()))
            {
                total += transaction.getAmount();
            }
        }

        return total;
    }


    public List<Transaction> getTransactions()
    {
        return new ArrayList<Transaction>(transactions);
    }


    public boolean startNewMonth(LocalDate firstDay)
    {
        if (firstDay == null
            || firstDay.getDayOfMonth() != 1)
        {
            return false;
        }

        ArrayList<Transaction> nextMonth =
            new ArrayList<Transaction>();

        for (Transaction transaction : transactions)
        {
            if (transaction.isRecurring())
            {
                Transaction carriedTransaction =
                    new Transaction(
                        transaction.getId(),
                        transaction.getDescription(),
                        transaction.getAmount(),
                        transaction.getType(),
                        transaction.getCategory(),
                        firstDay,
                        true);

                nextMonth.add(carriedTransaction);
            }
        }

        transactions = nextMonth;
        return true;
    }
}