public class BudgetPlan
{
    private final double monthlyIncome;


    public BudgetPlan(double monthlyIncome)
    {
        if (monthlyIncome < 0
            || Double.isNaN(monthlyIncome)
            || Double.isInfinite(monthlyIncome))
        {
            throw new IllegalArgumentException(
                "Monthly income must be nonnegative.");
        }

        this.monthlyIncome = monthlyIncome;
    }


    public double getTarget(String group)
    {
        if (group == null)
        {
            throw new IllegalArgumentException(
                "Budget group cannot be null.");
        }

        if (group.equalsIgnoreCase("Needs"))
        {
            return monthlyIncome * 0.50;
        }

        if (group.equalsIgnoreCase("Wants"))
        {
            return monthlyIncome * 0.30;
        }

        if (group.equalsIgnoreCase("Savings"))
        {
            return monthlyIncome * 0.20;
        }

        throw new IllegalArgumentException(
            "Invalid budget group.");
    }


    public boolean isWithinTarget(
        String group,
        double actual)
    {
        if (actual < 0
            || Double.isNaN(actual)
            || Double.isInfinite(actual))
        {
            throw new IllegalArgumentException(
                "Actual amount must be nonnegative.");
        }

        if (group == null)
        {
            throw new IllegalArgumentException(
                "Budget group cannot be null.");
        }

        if (group.equalsIgnoreCase("Needs")
            || group.equalsIgnoreCase("Wants"))
        {
            return actual <= getTarget(group);
        }

        if (group.equalsIgnoreCase("Savings"))
        {
            return actual >= getTarget(group);
        }

        throw new IllegalArgumentException(
            "Invalid budget group.");
    }


    public String getRecommendation(
        double needsSpent,
        double wantsSpent,
        double saved)
    {
        validateActual(needsSpent);
        validateActual(wantsSpent);
        validateActual(saved);

        boolean needsOk =
            isWithinTarget("Needs", needsSpent);

        boolean wantsOk =
            isWithinTarget("Wants", wantsSpent);

        boolean savingsOk =
            isWithinTarget("Savings", saved);

        if (needsOk && wantsOk && savingsOk)
        {
            return "Great job! Your budget follows the "
                + "50/30/20 plan.";
        }

        String recommendation = "";

        if (!needsOk)
        {
            recommendation +=
                "Reduce needs spending if possible. ";
        }

        if (!wantsOk)
        {
            recommendation +=
                "Try reducing wants spending. ";
        }

        if (!savingsOk)
        {
            recommendation +=
                "Try saving more toward your 20% savings goal.";
        }

        return recommendation.trim();
    }


    private void validateActual(double amount)
    {
        if (amount < 0
            || Double.isNaN(amount)
            || Double.isInfinite(amount))
        {
            throw new IllegalArgumentException(
                "Budget amount must be nonnegative.");
        }
    }
}