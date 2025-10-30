package utils;

public class Decimal
{
    double num;

    public Decimal(double input)
    {
        num = input;
    }
    public double roundToNPlace(int place)
    {
        double scale = Math.pow(10, place);
        return Math.round(num * scale) / scale;
    }
}