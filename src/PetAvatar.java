public class PetAvatar
{
    private final String name;
    private int level;


    public PetAvatar(String name)
    {
        if (!InputValidator.isValidText(name))
        {
            throw new IllegalArgumentException(
                "Pet name cannot be blank.");
        }

        this.name = name.trim();
        this.level = 1;
    }


    public void updateLevel(
        boolean withinNeedsAndWants,
        boolean savingsGoalMet)
    {
        if (withinNeedsAndWants && savingsGoalMet)
        {
            level++;
        }
        else if (!withinNeedsAndWants
            && !savingsGoalMet)
        {
            level--;
        }

        if (level < 1)
        {
            level = 1;
        }

        if (level > 5)
        {
            level = 5;
        }
    }


    public String getName()
    {
        return name;
    }


    public int getLevel()
    {
        return level;
    }


    public String getAsciiArt()
    {
        switch (level)
        {
            case 1:
                return "  (._.)\n"
                    + "  /|_|\\\n"
                    + "   / \\";

            case 2:
                return "  (^._.^)\n"
                    + "  /|   |\\\n"
                    + "   / \\";

            case 3:
                return "  ( ^_^ )\n"
                    + "  /|   |\\\n"
                    + "   / \\";

            case 4:
                return " \\(^_^)/\n"
                    + "    |  \n"
                    + "   / \\";

            case 5:
                return "* \\(^o^)/ *\n"
                    + "     |     \n"
                    + "    / \\";

            default:
                return "(._.)";
        }
    }


    public String getStatus()
    {
        switch (level)
        {
            case 1:
                return name + " is just getting started.";

            case 2:
                return name + " is making progress!";

            case 3:
                return name + " is building healthy habits!";

            case 4:
                return name + " is doing a great job!";

            case 5:
                return name + " is a budgeting master!";

            default:
                return name + " is ready to budget.";
        }
    }
}