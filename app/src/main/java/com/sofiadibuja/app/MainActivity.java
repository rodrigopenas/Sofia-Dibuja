package com.sofiadibuja.app;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.graphics.*;
import android.content.Context;
import android.view.*;
import android.widget.*;
import java.util.Locale;

public class MainActivity extends Activity {
  private TextToSpeech voz;
  private int paso=0;
  private final String[] frases={
    "Vamos a dibujar un pez. Poné tu hoja en horizontal. Dibujá un óvalo grande en el centro.",
    "Ahora, agregá una cola con forma de triángulo a la derecha del óvalo.",
    "Dibujá un pequeño círculo a la izquierda para hacer el ojo.",
    "Hacé una curva pequeña cerca del ojo para dibujar la boca.",
    "Agregá una aleta arriba y otra abajo del cuerpo.",
    "Terminá con tres líneas suaves en la cola. ¡Muy bien! Ya dibujaste tu pez."
  };
  private TextView titulo, instruccion;
  private Drawing dibujo;
  @Override public void onCreate(Bundle b){super.onCreate(b);
    voz=new TextToSpeech(this, status->{if(status==TextToSpeech.SUCCESS){voz.setLanguage(new Locale("es","AR")); voz.setSpeechRate(0.85f);}});
    LinearLayout root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(18,20,18,12);root.setBackgroundColor(Color.rgb(252,250,245));
    titulo=new TextView(this);titulo.setTextSize(25);titulo.setTextColor(Color.rgb(35,48,65));titulo.setGravity(Gravity.CENTER);root.addView(titulo);
    dibujo=new Drawing(this);root.addView(dibujo,new LinearLayout.LayoutParams(-1,0,1));
    instruccion=new TextView(this);instruccion.setTextSize(19);instruccion.setTextColor(Color.rgb(30,30,30));instruccion.setPadding(12,12,12,18);root.addView(instruccion);
    LinearLayout botones=new LinearLayout(this);botones.setOrientation(LinearLayout.HORIZONTAL);
    Button anterior=new Button(this);anterior.setText("Anterior");botones.addView(anterior,new LinearLayout.LayoutParams(0,-2,1));
    Button escuchar=new Button(this);escuchar.setText("Escuchar");botones.addView(escuchar,new LinearLayout.LayoutParams(0,-2,1));
    Button siguiente=new Button(this);siguiente.setText("Siguiente");botones.addView(siguiente,new LinearLayout.LayoutParams(0,-2,1));
    root.addView(botones);setContentView(root);
    anterior.setOnClickListener(v->{if(paso>0)paso--;actualizar();hablar();});
    siguiente.setOnClickListener(v->{if(paso<5)paso++;else paso=0;actualizar();hablar();});
    escuchar.setOnClickListener(v->hablar());actualizar();
  }
  private void actualizar(){titulo.setText("Sofía Dibuja · Paso "+(paso+1)+" de 6");instruccion.setText(frases[paso]);dibujo.invalidate();}
  private void hablar(){if(voz!=null)voz.speak(frases[paso],TextToSpeech.QUEUE_FLUSH,null,"paso");}
  @Override protected void onDestroy(){if(voz!=null){voz.stop();voz.shutdown();}super.onDestroy();}
  class Drawing extends View {
    Paint pen=new Paint(3);
    Drawing(Context c){super(c);setBackgroundColor(Color.WHITE);pen.setColor(Color.rgb(35,73,104));pen.setStrokeWidth(5);pen.setStyle(Paint.Style.STROKE);pen.setStrokeCap(Paint.Cap.ROUND);pen.setStrokeJoin(Paint.Join.ROUND);}
    void path(Canvas c,float... pts){Path p=new Path();p.moveTo(pts[0],pts[1]);for(int i=2;i<pts.length;i+=2)p.lineTo(pts[i],pts[i+1]);c.drawPath(p,pen);}
    @Override protected void onDraw(Canvas original){super.onDraw(original);Canvas c=original;int save=c.save();float scale=Math.min(getWidth()/400f,getHeight()/320f);c.translate((getWidth()-400*scale)/2,(getHeight()-320*scale)/2);c.scale(scale,scale);
      c.drawOval(65,100,295,240,pen);
      if(paso>=1)path(c,290,170,375,105,375,235,290,170);
      if(paso>=2){pen.setStyle(Paint.Style.FILL);c.drawCircle(118,149,8,pen);pen.setStyle(Paint.Style.STROKE);}
      if(paso>=3)c.drawArc(70,164,113,200,0,140,false,pen);
      if(paso>=4){path(c,165,103,201,60,236,104);path(c,165,239,204,275,239,231);}
      if(paso>=5){path(c,322,147,357,137);path(c,326,170,361,170);path(c,322,192,357,203);}
      c.restoreToCount(save);
    }
  }
}
