package sen.sen.prototypesen.CustomUI;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import sen.sen.prototypesen.Cells.NumberCell;
import sen.sen.prototypesen.R;

public class NumberListVIew<T> extends RecyclerView{
  private static final float MILLISECONDS_PER_INCH = 100f;

  private List<T> numberList;
  private int currentPosition;

  RecyclerView.SmoothScroller smoothScroller;

  public NumberListVIew(Context context){
    super(context);
    initialize();
  }

  public NumberListVIew(Context context, @Nullable AttributeSet attrs){
    super(context, attrs);
    initialize();
  }

  public NumberListVIew(Context context, @Nullable AttributeSet attrs, int defStyle){
    super(context, attrs, defStyle);
    initialize();
  }

  public void updateListAndPosition(List<T> numberList, int position){
    this.numberList = numberList;
    this.currentPosition = position;
    layoutView();
  }

  private void initialize(){
    setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    setAdapter(new RecyclerViewAdapter());

    smoothScroller = new LinearSmoothScroller(getContext()){

      @Override
      protected int getHorizontalSnapPreference(){
        return LinearSmoothScroller.SNAP_TO_START;
      }

      @Override
      protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
        return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
      }
    };
  }

  private void layoutView(){
    if((numberList == null) || (numberList.size() == 0)){
      return;
    }
    getAdapter().notifyDataSetChanged();
    smoothScroller.setTargetPosition(currentPosition);
    getLayoutManager().startSmoothScroll(smoothScroller);
  }

  @Override
  public boolean onTouchEvent(MotionEvent e){
    return false;
  }

  private class RecyclerViewAdapter extends RecyclerView.Adapter<NumberCell<T>>{
    @Override
    public NumberCell<T> onCreateViewHolder(ViewGroup parent, int viewType){
      return new NumberCell<T>(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_number, parent, false));
    }

    @Override
    public void onBindViewHolder(NumberCell<T> numberCell, int position){
      numberCell.bind(numberList.get(position), position == currentPosition);
    }

    @Override
    public int getItemCount(){
      return numberList.size();
    }
  }
}
