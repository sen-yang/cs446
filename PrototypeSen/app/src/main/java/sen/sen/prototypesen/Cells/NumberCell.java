package sen.sen.prototypesen.Cells;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.text.NumberFormat;

import sen.sen.prototypesen.R;

public class NumberCell<T> extends RecyclerView.ViewHolder{
  private TextView numberTextView;
  public NumberCell(View itemView){
    super(itemView);
    numberTextView = itemView.findViewById(R.id.numberTextView);
  }

  public void bind(T number, boolean isCurrent){
    if(isCurrent){
      numberTextView.setBackgroundResource(R.drawable.background_number_cell_current);

    }
    else{
      numberTextView.setBackgroundResource(R.drawable.background_number_cell);
    }
    numberTextView.setText(NumberFormat.getInstance().format(number));
  }
}
