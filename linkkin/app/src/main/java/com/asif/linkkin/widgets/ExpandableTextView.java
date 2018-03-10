package com.asif.linkkin.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.widget.TextView;

import com.asif.linkkin.R;

public class ExpandableTextView
  extends TextView
{
  private static final int DEFAULT_TRIM_LENGTH = 100;
  private static final String ELLIPSIS = ".....";
  private BufferType bufferType;
  private CharSequence originalText;
  private boolean trim = true;
  private int trimLength;
  private CharSequence trimmedText;
  
  public ExpandableTextView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public ExpandableTextView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    TypedArray paramContext1 = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ExpandableTextView);
    this.trimLength = paramContext1.getInt(0, 100);
    paramContext1.recycle();
  }
  
  private CharSequence getDisplayableText()
  {
    if (this.trim) {
      return this.trimmedText;
    }
    return this.originalText;
  }
  
  private CharSequence getTrimmedText(CharSequence paramCharSequence)
  {
    if ((this.originalText != null) && (this.originalText.length() > this.trimLength)) {
      return new SpannableStringBuilder(this.originalText, 0, this.trimLength + 1).append(".....");
    }
    return this.originalText;
  }
  
  private void setText()
  {
    super.setText(getDisplayableText(), this.bufferType);
  }
  
  public CharSequence getOriginalText()
  {
    return this.originalText;
  }
  
  public int getTrimLength()
  {
    return this.trimLength;
  }
  
  public void setText(CharSequence paramCharSequence, BufferType paramBufferType)
  {
    this.originalText = paramCharSequence;
    this.trimmedText = getTrimmedText(paramCharSequence);
    this.bufferType = paramBufferType;
    setText();
  }
  
  public void setTrimLength(int paramInt)
  {
    this.trimLength = paramInt;
    this.trimmedText = getTrimmedText(this.originalText);
    setText();
  }
  
  public void textClicked()
  {
    /*
    if (!this.trim) {}
    for (boolean bool = true;; bool = false)
    {
      this.trim = bool;
      setText();
      requestFocusFromTouch();
      return;
    }    */
    if(!trim)
    {
      trim=true;
    }
    else trim=false;
    setText();
    requestFocusFromTouch();
  }
}

