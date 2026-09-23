import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class PennyPetApp
{
    private Scanner input;
    private BudgetManager manager;
    private PetAvatar pet;
    private int nextId;


    public PennyPetApp(Scanner input)
    {
        if (input == null)
        {
            throw new IllegalArgumentException(
                "Scanner cannot be null.");
        }

        this.input = input;
        this.manager = new BudgetManager();
        this.pet = new PetAvatar("Penny");
        this.nextId = 1;
    }


    public void run()
    {
        boolean running = true;

        System.out.println("=========================");
        System.out.println("        PENNY PET");
        System.out.println("=========================");

        while (running)
        {
            printMenu();

            if (!input.hasNextLine())
            {
                break;
            }

            String choice = input.nextLine().trim();

            switch (choice)
            {
                case "1":
                    addTransaction();
                    break;

                case "2":
                    editTransaction();
                    break;

                case "3":
                    removeTransaction();
                    break;

                case "4":
                    viewTransactions();
                    break;

                case "5":
                    showMonthlySummary();
                    break;

                case "6":
                    startNewMonth();
                    break;

                case "7":
                    viewPet();
                    break;

                case "0":
                    running = false;
                    System.out.println("Goodbye!");
                    break;

                default:
                    System.out.println(
                        "Please enter a valid menu option.");
                    break;
            }
        }
    }


    private void printMenu()
    {
        System.out.println();
        System.out.println("1. Add Transaction");
        System.out.println("2. Edit Transaction");
        System.out.println("3. Remove Transaction");
        System.out.println("4. View Transactions");
        System.out.println("5. Monthly Summary");
        System.out.println("6. Start New Month");
        System.out.println("7. View Pet");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }


    private void addTransaction()
    {
        String description =
            readText("Description: ");

        if (description == null)
        {
            return;
        }

        TransactionType type =
            readTransactionType();

        if (type == null)
        {
            return;
        }

        String category =
            readCategory(type);

        if (category == null)
        {
            return;
        }

        Double amount =
            readAmount("Amount: $");

        if (amount == null)
        {
            return;
        }

        LocalDate date =
            readDate();

        if (date == null)
        {
            return;
        }

        Boolean recurring =
            readYesNo(
                "Recurring monthly? (Y/N): ");

        if (recurring == null)
        {
            return;
        }

        Transaction transaction =
            new Transaction(
                nextId,
                description,
                amount,
                type,
                category,
                date,
                recurring);

        if (manager.addTransaction(transaction))
        {
            System.out.println(
                "Transaction added. ID: " + nextId);

            nextId++;
        }
        else
        {
            System.out.println(
                "Unable to add transaction.");
        }
    }


    private void editTransaction()
    {
        if (manager.getTransactions().isEmpty())
        {
            System.out.println(
                "There are no transactions to edit.");
            return;
        }

        viewTransactions();

        Integer id =
            readTransactionId(
                "Transaction ID to edit: ");

        if (id == null)
        {
            return;
        }

        Transaction oldTransaction =
            manager.findTransaction(id);

        System.out.println(
            "Editing: "
                + oldTransaction.getDescription());

        String description =
            readText("New description: ");

        if (description == null)
        {
            return;
        }

        TransactionType type =
            readTransactionType();

        if (type == null)
        {
            return;
        }

        String category =
            readCategory(type);

        if (category == null)
        {
            return;
        }

        Double amount =
            readAmount("New amount: $");

        if (amount == null)
        {
            return;
        }

        LocalDate date =
            readDate();

        if (date == null)
        {
            return;
        }

        Boolean recurring =
            readYesNo(
                "Recurring monthly? (Y/N): ");

        if (recurring == null)
        {
            return;
        }

        Transaction replacement =
            new Transaction(
                id,
                description,
                amount,
                type,
                category,
                date,
                recurring);

        if (manager.editTransaction(
            id, replacement))
        {
            System.out.println(
                "Transaction updated.");
        }
        else
        {
            System.out.println(
                "Unable to edit transaction.");
        }
    }


    private void removeTransaction()
    {
        if (manager.getTransactions().isEmpty())
        {
            System.out.println(
                "There are no transactions to remove.");
            return;
        }

        viewTransactions();

        Integer id =
            readTransactionId(
                "Transaction ID to remove: ");

        if (id == null)
        {
            return;
        }

        Boolean confirmed =
            readYesNo(
                "Are you sure you want to remove "
                    + "this transaction? (Y/N): ");

        if (confirmed == null)
        {
            return;
        }

        if (!confirmed)
        {
            System.out.println(
                "Removal cancelled.");
            return;
        }

        if (manager.removeTransaction(id))
        {
            System.out.println(
                "Transaction removed.");
        }
        else
        {
            System.out.println(
                "Unable to remove transaction.");
        }
    }


    private void viewTransactions()
    {
        List<Transaction> transactions =
            manager.getTransactions();

        if (transactions.isEmpty())
        {
            System.out.println(
                "No transactions recorded.");
            return;
        }

        System.out.println();
        System.out.println(
            "===== TRANSACTIONS =====");

        for (Transaction transaction : transactions)
        {
            System.out.println(
                "ID: " + transaction.getId());

            System.out.println(
                "Description: "
                    + transaction.getDescription());

            System.out.printf(
                "Amount: $%.2f%n",
                transaction.getAmount());

            System.out.println(
                "Type: " + transaction.getType());

            System.out.println(
                "Category: "
                    + transaction.getCategory());

            System.out.println(
                "Date: " + transaction.getDate());

            System.out.println(
                "Recurring: "
                    + transaction.isRecurring());

            System.out.println(
                "------------------------");
        }
    }


    private void showMonthlySummary()
    {
        double income =
            manager.getTotal(
                TransactionType.INCOME);

        double expenses =
            manager.getTotal(
                TransactionType.EXPENSE);

        double balance =
            manager.getBalance();

        double needs =
            getNeedsTotal();

        double wants =
            getWantsTotal();

        double savings =
            getSavingsTotal();

        BudgetPlan plan =
            new BudgetPlan(income);

        System.out.println();
        System.out.println(
            "===== MONTHLY SUMMARY =====");

        System.out.printf(
            "Income:   $%.2f%n",
            income);

        System.out.printf(
            "Expenses: $%.2f%n",
            expenses);

        System.out.printf(
            "Balance:  $%.2f%n",
            balance);

        System.out.println();

        System.out.printf(
            "Needs:   $%.2f / $%.2f target%n",
            needs,
            plan.getTarget("Needs"));

        System.out.printf(
            "Wants:   $%.2f / $%.2f target%n",
            wants,
            plan.getTarget("Wants"));

        System.out.printf(
            "Savings: $%.2f / $%.2f target%n",
            savings,
            plan.getTarget("Savings"));

        System.out.println();

        System.out.println(
            "Recommendation:");

        System.out.println(
            plan.getRecommendation(
                needs,
                wants,
                savings));
    }


    private void startNewMonth()
    {
        LocalDate firstDay =
            readFirstDayOfMonth();

        if (firstDay == null)
        {
            return;
        }

        Boolean confirmed =
            readYesNo(
                "Start new month? (Y/N): ");

        if (confirmed == null)
        {
            return;
        }

        if (!confirmed)
        {
            System.out.println(
                "New month cancelled.");
            return;
        }

        updatePetFromBudget();

        if (manager.startNewMonth(firstDay))
        {
            System.out.println(
                "New month started.");

            System.out.println(
                "One-time transactions were removed.");

            System.out.println(
                "Recurring transactions were carried forward.");
        }
        else
        {
            System.out.println(
                "Unable to start new month.");
        }
    }


    private void viewPet()
    {
        System.out.println();
        System.out.println(
            "===== YOUR PET =====");

        System.out.println(
            "Name: " + pet.getName());

        System.out.println(
            "Level: " + pet.getLevel());

        System.out.println(
            pet.getAsciiArt());

        System.out.println(
            pet.getStatus());
    }


    private void updatePetFromBudget()
    {
        double income =
            manager.getTotal(
                TransactionType.INCOME);

        if (income <= 0)
        {
            return;
        }

        BudgetPlan plan =
            new BudgetPlan(income);

        double needs =
            getNeedsTotal();

        double wants =
            getWantsTotal();

        double savings =
            getSavingsTotal();

        boolean needsAndWantsOk =
            plan.isWithinTarget(
                "Needs", needs)
                && plan.isWithinTarget(
                    "Wants", wants);

        boolean savingsOk =
            plan.isWithinTarget(
                "Savings", savings);

        pet.updateLevel(
            needsAndWantsOk,
            savingsOk);
    }


    private double getNeedsTotal()
    {
        return manager.getCategoryTotal("Housing")
            + manager.getCategoryTotal("Insurance")
            + manager.getCategoryTotal("Utilities")
            + manager.getCategoryTotal("Groceries")
            + manager.getCategoryTotal(
                "Transportation");
    }


    private double getWantsTotal()
    {
        return manager.getCategoryTotal(
            "Entertainment")
            + manager.getCategoryTotal(
                "Subscriptions")
            + manager.getCategoryTotal(
                "Fast Food")
            + manager.getCategoryTotal(
                "Miscellaneous");
    }


    private double getSavingsTotal()
    {
        return manager.getCategoryTotal(
            "Emergency Fund")
            + manager.getCategoryTotal(
                "Rainy Day");
    }


    private String readText(String prompt)
    {
        while (true)
        {
            System.out.print(prompt);

            if (!input.hasNextLine())
            {
                return null;
            }

            String text =
                input.nextLine().trim();

            if (InputValidator.isValidText(text))
            {
                return text;
            }

            System.out.println(
                "Input cannot be blank.");
        }
    }


    private Double readAmount(String prompt)
    {
        while (true)
        {
            System.out.print(prompt);

            if (!input.hasNextLine())
            {
                return null;
            }

            String text =
                input.nextLine().trim();

            try
            {
                double amount =
                    Double.parseDouble(text);

                if (!InputValidator
                    .isValidAmount(amount))
                {
                    System.out.println(
                        "Enter a valid amount greater "
                            + "than 0 and at most 1,000,000.");
                    continue;
                }

                if (InputValidator
                    .requiresConfirmation(amount))
                {
                    Boolean confirmed =
                        readYesNo(
                            String.format(
                                "You entered $%.2f. "
                                    + "Is this correct? (Y/N): ",
                                amount));

                    if (confirmed == null)
                    {
                        return null;
                    }

                    if (!confirmed)
                    {
                        continue;
                    }
                }

                return amount;
            }
            catch (NumberFormatException e)
            {
                System.out.println(
                    "Enter a valid number.");
            }
        }
    }


    private TransactionType readTransactionType()
    {
        while (true)
        {
            System.out.println(
                "1. Income");

            System.out.println(
                "2. Expense");

            System.out.print(
                "Transaction type: ");

            if (!input.hasNextLine())
            {
                return null;
            }

            String choice =
                input.nextLine().trim();

            if (choice.equals("1"))
            {
                return TransactionType.INCOME;
            }

            if (choice.equals("2"))
            {
                return TransactionType.EXPENSE;
            }

            System.out.println(
                "Please enter 1 or 2.");
        }
    }


    private String readCategory(
        TransactionType type)
    {
        while (true)
        {
            if (type == TransactionType.INCOME)
            {
                System.out.println(
                    "1. Salary");
                System.out.println(
                    "2. Other Income");

                System.out.print(
                    "Choose a category: ");

                if (!input.hasNextLine())
                {
                    return null;
                }

                String choice =
                    input.nextLine().trim();

                if (choice.equals("1"))
                {
                    return "Salary";
                }

                if (choice.equals("2"))
                {
                    return "Other Income";
                }
            }
            else
            {
                System.out.println(
                    "1. Housing");
                System.out.println(
                    "2. Insurance");
                System.out.println(
                    "3. Utilities");
                System.out.println(
                    "4. Groceries");
                System.out.println(
                    "5. Transportation");
                System.out.println(
                    "6. Entertainment");
                System.out.println(
                    "7. Subscriptions");
                System.out.println(
                    "8. Fast Food");
                System.out.println(
                    "9. Miscellaneous");
                System.out.println(
                    "10. Emergency Fund");
                System.out.println(
                    "11. Rainy Day");

                System.out.print(
                    "Choose a category: ");

                if (!input.hasNextLine())
                {
                    return null;
                }

                String choice =
                    input.nextLine().trim();

                switch (choice)
                {
                    case "1":
                        return "Housing";

                    case "2":
                        return "Insurance";

                    case "3":
                        return "Utilities";

                    case "4":
                        return "Groceries";

                    case "5":
                        return "Transportation";

                    case "6":
                        return "Entertainment";

                    case "7":
                        return "Subscriptions";

                    case "8":
                        return "Fast Food";

                    case "9":
                        return "Miscellaneous";

                    case "10":
                        return "Emergency Fund";

                    case "11":
                        return "Rainy Day";

                    default:
                        break;
                }
            }

            System.out.println(
                "Please enter a valid category number.");
        }
    }


    private LocalDate readDate()
    {
        while (true)
        {
            System.out.print(
                "Date (YYYY-MM-DD): ");

            if (!input.hasNextLine())
            {
                return null;
            }

            String dateText =
                input.nextLine().trim();

            if (InputValidator
                .isValidDate(dateText))
            {
                return LocalDate.parse(dateText);
            }

            System.out.println(
                "Enter a valid date in YYYY-MM-DD "
                    + "format, for example 2026-09-23.");
        }
    }


    private LocalDate readFirstDayOfMonth()
    {
        while (true)
        {
            System.out.print(
                "First day of new month "
                    + "(YYYY-MM-DD): ");

            if (!input.hasNextLine())
            {
                return null;
            }

            String dateText =
                input.nextLine().trim();

            if (!InputValidator
                .isValidDate(dateText))
            {
                System.out.println(
                    "Enter a valid date in YYYY-MM-DD "
                        + "format.");
                continue;
            }

            LocalDate date =
                LocalDate.parse(dateText);

            if (date.getDayOfMonth() != 1)
            {
                System.out.println(
                    "The date must be the first "
                        + "day of a month.");
                continue;
            }

            return date;
        }
    }


    private Integer readTransactionId(
        String prompt)
    {
        while (true)
        {
            System.out.print(prompt);

            if (!input.hasNextLine())
            {
                return null;
            }

            String text =
                input.nextLine().trim();

            try
            {
                int id =
                    Integer.parseInt(text);

                if (InputValidator
                    .isValidTransactionId(
                        id, manager))
                {
                    return id;
                }

                System.out.println(
                    "Transaction ID does not exist.");
            }
            catch (NumberFormatException e)
            {
                System.out.println(
                    "Enter a valid number.");
            }
        }
    }


    private Boolean readYesNo(String prompt)
    {
        while (true)
        {
            System.out.print(prompt);

            if (!input.hasNextLine())
            {
                return null;
            }

            String answer =
                input.nextLine().trim();

            if (answer.equalsIgnoreCase("Y"))
            {
                return true;
            }

            if (answer.equalsIgnoreCase("N"))
            {
                return false;
            }

            System.out.println(
                "Please enter Y or N.");
        }
    }


    public static void main(String[] args)
    {
        Scanner scanner =
            new Scanner(System.in);

        PennyPetApp app =
            new PennyPetApp(scanner);

        app.run();

        scanner.close();
    }
}