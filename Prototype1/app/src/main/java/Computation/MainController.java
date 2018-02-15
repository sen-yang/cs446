package Computation;

/**
 * Created by ASUS on 8/2/2018.
 */

public class MainController {

    Calculation Add = new AddCalculation();
    Calculation Minus = new MinusCalculation();
    Calculation Divide = new DivideCalculation();
    Calculation Multiple = new MultipleCalculation();

    public int Calculate(int original, int next, String arith){
        if(arith=="+")
            return (Add.Calculate(original, next));
        else if(arith=="-")
            return (Minus.Calculate(original, next));
        else if(arith=="/")
            return (Divide.Calculate(original, next));
        else if(arith=="*")
            return (Multiple.Calculate(original, next));
    else
        return -1;

    }

}
