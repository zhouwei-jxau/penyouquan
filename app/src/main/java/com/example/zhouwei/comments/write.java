package com.example.zhouwei.comments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import com.network.zhouwei.http_network.Server;
import java.util.ArrayList;
import java.util.List;

public class write extends AppCompatActivity {
    public static final int CHOOSELOCATIONREQUESETCODE=2;
    private int imageCounter=0;
    private ImageView[] images;
    private EditText editText;
    private Activity this_activity=null;
    private Button button_location;
    private Spinner spinner_tag=null;
    private List<String> tagList;

    private Thread thread_publish=new Thread()
    {
      public void run(){
          Server server=new Server();
          User u=CurrentUser.getUser();
          Bitmap[] bitmaps=new Bitmap[imageCounter];
          for(int i=0;i<imageCounter;i++)
          {
              bitmaps[i]=((BitmapDrawable)images[i].getDrawable()).getBitmap();
          }
          String s_location="";
          if(!(button_location.getText().toString().equals("当前位置:无法识别")||button_location.getText().toString().equals("选择地点")))
          {
              s_location=button_location.getText().toString();
          }
          String s_tag="";
          if(spinner_tag.getSelectedItemPosition()==0)
          {
              s_tag="";
          }
          else
          {
              s_tag=tagList.get(spinner_tag.getSelectedItemPosition());
          }
          server.commnetResourcesUpload(u.getUsername(),editText.getText().toString(),bitmaps,s_location,s_tag);
      }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        tagList=new ArrayList<String>();
        tagList.add("不添加标签");
        tagList.add("一叶落寞，万物失色。");
        tagList.add("望顶繁花，如水似流。");
        tagList.add("生死挈阔，与子成说。");
        tagList.add("寂寞，只愿一人独享。");
        tagList.add("知己知彼，百战不殆。");
        tagList.add("既已伤，何故空留痕。");
        tagList.add("雪落无痕，雁过留声。");

        spinner_tag=(Spinner)findViewById(R.id.spinner_tag);
        SpinnerAdapter spinnerAdapter=new ArrayAdapter<String>(this,R.layout.spinner_drop,this.tagList);
        spinner_tag.setAdapter(spinnerAdapter);

        this_activity=this;

        ImageView[] t={
                (ImageView)findViewById(R.id.image1),
                (ImageView)findViewById(R.id.image2),
                (ImageView)findViewById(R.id.image3),
                (ImageView)findViewById(R.id.image4),
                (ImageView)findViewById(R.id.image5),
                (ImageView)findViewById(R.id.image6),
                (ImageView)findViewById(R.id.image7),
                (ImageView)findViewById(R.id.image8),
                (ImageView)findViewById(R.id.image9)
        };

        images=t;

        ImageView button_back=(ImageView)findViewById(R.id.imageView_backToMonent);
        ImageView button_publish=(ImageView)findViewById(R.id.imageView_publish);
        button_location=(Button)findViewById(R.id.location);

        button_back.setOnClickListener(v->{
            Intent intent=new Intent();
            intent.setClass(this,comment.class);
            startActivity(intent);
        });

        button_publish.setOnClickListener(v-> {
            Toast.makeText(getApplicationContext(), "发布成功", Toast.LENGTH_SHORT).show();
            Thread thread=new Thread(thread_publish);
            thread.start();
            while(thread.isAlive());
            Intent intent=new Intent();
            intent.setClass(this_activity,comment.class);
            startActivity(intent);
        });

        button_location.setOnClickListener(v->{
            Intent intent=new Intent();
            intent.setClass(getApplicationContext(),ChooseLocation.class);
            startActivityForResult(intent,CHOOSELOCATIONREQUESETCODE);
        });

        for(int i=0;i<images.length;i++)
        {
            images[i].setOnClickListener(v->{
                imageButtonClick(v);
            });
            images[i].setVisibility(ImageView.INVISIBLE);
        }
        images[0].setImageResource(R.drawable.image_add);
        images[0].setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        images[0].setVisibility(ImageView.VISIBLE);

        editText=(EditText)findViewById(R.id.editor);
    }

    public void addImage(Uri uri)
    {
        if(imageCounter>=9)
        {
            return;
        }
        images[imageCounter].setImageURI(uri);
        images[imageCounter].setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        images[imageCounter].setVisibility(ImageView.VISIBLE);
        if(imageCounter+1<=7)
        {
            images[imageCounter+1].setImageResource(R.drawable.image_add);
            images[imageCounter+1].setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            images[imageCounter+1].setVisibility(ImageView.VISIBLE);
        }
        imageCounter++;
        if(imageCounter>=3)
        {
            LinearLayout linearLayout=(LinearLayout)findViewById(R.id.layout_image_2);
            LinearLayout.LayoutParams layoutParams=(LinearLayout.LayoutParams) linearLayout.getLayoutParams();
            layoutParams.height=LinearLayout.LayoutParams.WRAP_CONTENT;
            linearLayout.setLayoutParams(layoutParams);
        }
        if(imageCounter>=6)
        {
            LinearLayout linearLayout=(LinearLayout)findViewById(R.id.layout_image_3);
            LinearLayout.LayoutParams layoutParams=(LinearLayout.LayoutParams) linearLayout.getLayoutParams();
            layoutParams.height=LinearLayout.LayoutParams.WRAP_CONTENT;
            linearLayout.setLayoutParams(layoutParams);
        }
    }

    public void imageButtonClick(View v)
    {
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE );
        startActivityForResult(intent,1);
    }

    protected  void onActivityResult(int requestCode,int resultCode,Intent data) {
        if(resultCode== Activity.RESULT_OK)
        {
            if(requestCode==1)
            {
                Uri uri=data.getData();
                this.addImage(uri);
            }
            if(requestCode==CHOOSELOCATIONREQUESETCODE)
            {
                String location=data.getExtras().getString("location");
                button_location.setText(location);
            }
        }
    }
}
