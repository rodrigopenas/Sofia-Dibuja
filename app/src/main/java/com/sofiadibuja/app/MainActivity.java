package com.sofiadibuja.app;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.graphics.*;
import android.content.Context;
import android.view.*;
import android.widget.*;
import java.util.*;

public class MainActivity extends Activity {
  TextToSpeech voz;
  int paso=0, elegido=-1;
  LinearLayout raiz;
  TextView titulo, instruccion;
  Drawing dibujo;
  final String[] nombres={"Pez","Gato","Perro","Mariposa","Caballo","Árbol","Flor","Montañas","Paisaje","Casa","Auto","Bicicleta","Cara","Persona","Robot"};
  final String[] categorias={"Animales","Animales","Animales","Animales","Animales","Naturaleza","Naturaleza","Naturaleza","Naturaleza","Objetos","Objetos","Objetos","Personajes","Personajes","Personajes"};
  final String[][] frases={
    {"Dibujá un óvalo grande en el centro de la hoja.","Agregá una cola triangular a la derecha.","Poné un ojo redondo a la izquierda.","Dibujá una boca pequeña.","Sumá una aleta arriba y otra abajo.","Terminá con líneas en la cola. ¡Muy bien!"},
    {"Dibujá un círculo grande para la cabeza.","Agregá dos orejas triangulares.","Dibujá dos ojos redondos.","Poné una nariz triangular y una boca.","Sumá bigotes a los costados.","Dibujá el cuerpo y una cola curva."},
    {"Dibujá un círculo para la cabeza.","Agregá dos orejas caídas.","Dibujá dos ojos.","Poné una nariz y una boca.","Dibujá un cuerpo ovalado.","Agregá cuatro patas y una cola."},
    {"Dibujá un cuerpo largo y finito en el centro.","Sumá las dos alas superiores.","Agregá las dos alas inferiores.","Dibujá una cabeza redonda.","Poné dos antenas.","Decorá las alas con círculos."},
    {"Dibujá un óvalo grande para el cuerpo.","Agregá el cuello y la cabeza.","Dibujá dos orejas y un ojo.","Sumá las cuatro patas.","Dibujá una cola larga.","Agregá la crin y los cascos."},
    {"Dibujá dos líneas para el tronco.","Uní las líneas abajo con una base.","Agregá una copa grande y redonda.","Dibujá dos ramas dentro de la copa.","Sumá hojas con círculos pequeños.","Dibujá el suelo debajo del árbol."},
    {"Dibujá un círculo para el centro de la flor.","Agregá pétalos alrededor.","Dibujá un tallo hacia abajo.","Sumá una hoja a la izquierda.","Agregá otra hoja a la derecha.","Dibujá pastito en la base."},
    {"Dibujá una línea horizontal para el suelo.","Dibujá una montaña triangular.","Agregá otra montaña al lado.","Dibujá nieve en las cumbres.","Sumá un sol redondo.","Agregá algunas nubes."},
    {"Dibujá una línea de horizonte.","Agregá dos montañas al fondo.","Dibujá un sol arriba.","Sumá un árbol a la izquierda.","Dibujá un camino curvo.","Agregá nubes y pastito."},
    {"Dibujá un cuadrado grande.","Agregá un techo triangular.","Dibujá una puerta rectangular.","Sumá dos ventanas.","Agregá una chimenea.","Dibujá un camino y el suelo."},
    {"Dibujá un rectángulo largo para el auto.","Agregá el techo con dos líneas.","Dibujá dos ruedas redondas.","Sumá dos ventanas.","Dibujá las luces.","Agregá una puerta y el suelo."},
    {"Dibujá dos ruedas grandes.","Uní las ruedas con el cuadro triangular.","Agregá el manubrio.","Dibujá el asiento.","Sumá los pedales.","Agregá los rayos de las ruedas."},
    {"Dibujá un óvalo para la cara.","Agregá dos ojos.","Dibujá una nariz pequeña.","Sumá una boca sonriente.","Agregá dos orejas.","Dibujá el pelo."},
    {"Dibujá un círculo para la cabeza.","Agregá una línea para el cuerpo.","Dibujá dos brazos.","Sumá dos piernas.","Agregá manos y pies.","Dibujá ojos, nariz y boca."},
    {"Dibujá un cuadrado para la cabeza del robot.","Agregá un rectángulo para el cuerpo.","Dibujá dos brazos.","Sumá dos piernas.","Agregá ojos y una boca.","Dibujá botones y una antena."}
  };
  @Override public void onCreate(Bundle b) {
    super.onCreate(b);
    voz=new TextToSpeech(this,s->{if(s==TextToSpeech.SUCCESS){voz.setLanguage(new Locale("es","AR"));voz.setSpeechRate(0.85f);}});
    menu();
  }
  int ink=Color.rgb(40,53,78), coral=Color.rgb(235,111,100), paper=Color.rgb(255,250,241);
  int dp(float n){return (int)(getResources().getDisplayMetrics().density*n+0.5f);}
  android.graphics.drawable.GradientDrawable bg(int color,int radius){
    android.graphics.drawable.GradientDrawable d=new android.graphics.drawable.GradientDrawable();
    d.setColor(color);d.setCornerRadius(dp(radius));return d;
  }
  TextView text(String value,int size,int color){
    TextView t=new TextView(this);t.setText(value);t.setTextSize(size);t.setTextColor(color);return t;
  }
  TextView action(String label,int color){
    TextView t=text(label,16,Color.WHITE);t.setGravity(Gravity.CENTER);
    t.setTypeface(null,1);t.setBackground(bg(color,18));t.setPadding(dp(9),dp(14),dp(9),dp(14));return t;
  }
  void base(){raiz=new LinearLayout(this);raiz.setOrientation(1);raiz.setPadding(dp(16),dp(16),dp(16),dp(16));raiz.setBackgroundColor(paper);setContentView(raiz);}
  void menu(){
    elegido=-1;paso=0;if(voz!=null)voz.stop();base();
    TextView brand=text("✏  SOFÍA DIBUJA",28,ink);brand.setTypeface(null,1);raiz.addView(brand);
    TextView sub=text("Tu pequeño taller de arte",17,coral);sub.setPadding(0,dp(3),0,dp(10));raiz.addView(sub);
    TextView tip=text("Prepará lápiz y papel. Elegí un dibujo y seguí los pasos en tu hoja.",15,ink);
    tip.setPadding(dp(15),dp(13),dp(15),dp(13));tip.setBackground(bg(Color.WHITE,18));raiz.addView(tip);
    ScrollView scroll=new ScrollView(this);scroll.setFillViewport(false);
    raiz.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
    LinearLayout list=new LinearLayout(this);list.setOrientation(1);list.setPadding(0,dp(6),0,dp(12));scroll.addView(list);
    String previous="";
    LinearLayout row=null;
    for(int i=0;i<nombres.length;i++){
      if(!categorias[i].equals(previous)){
        previous=categorias[i];TextView section=text(previous.toUpperCase(new Locale("es","AR")),17,ink);
        section.setTypeface(null,1);section.setPadding(dp(3),dp(19),0,dp(9));list.addView(section);
      }
      if(i==0||!categorias[i].equals(categorias[i-1])||row==null){
        row=new LinearLayout(this);row.setOrientation(0);list.addView(row);
      }
      final int index=i;
      LinearLayout card=new LinearLayout(this);card.setOrientation(1);card.setGravity(Gravity.CENTER);
      card.setPadding(dp(7),dp(7),dp(7),dp(10));card.setBackground(bg(Color.WHITE,18));
      LinearLayout.LayoutParams cp=new LinearLayout.LayoutParams(0,dp(154),1);
      cp.setMargins(dp(3),dp(4),dp(3),dp(4));row.addView(card,cp);
      Drawing thumb=new Drawing(this,index,true);card.addView(thumb,new LinearLayout.LayoutParams(-1,dp(105)));
      TextView label=text(nombres[i],16,ink);label.setTypeface(null,1);label.setGravity(Gravity.CENTER);card.addView(label);
      card.setOnClickListener(v->{elegido=index;paso=0;leccion();hablar();});
      if(row.getChildCount()==2)row=null;
    }
  }
  void leccion(){
    base();
    TextView back=text("‹  Todos los dibujos",16,coral);back.setTypeface(null,1);
    back.setPadding(dp(2),dp(5),0,dp(12));raiz.addView(back);back.setOnClickListener(v->menu());
    titulo=text("",25,ink);titulo.setTypeface(null,1);raiz.addView(titulo);
    TextView hint=text("Mirá el ejemplo y dibujalo en tu papel",14,coral);
    hint.setPadding(0,dp(3),0,dp(12));raiz.addView(hint);
    android.widget.FrameLayout panel=new android.widget.FrameLayout(this);panel.setBackground(bg(Color.WHITE,24));
    raiz.addView(panel,new LinearLayout.LayoutParams(-1,0,1));
    dibujo=new Drawing(this);panel.addView(dibujo,new android.widget.FrameLayout.LayoutParams(-1,-1));
    instruccion=text("",18,ink);instruccion.setGravity(Gravity.CENTER);
    instruccion.setBackground(bg(Color.WHITE,18));instruccion.setPadding(dp(14),dp(17),dp(14),dp(17));
    LinearLayout.LayoutParams ip=new LinearLayout.LayoutParams(-1,-2);ip.setMargins(0,dp(12),0,dp(12));raiz.addView(instruccion,ip);
    LinearLayout controls=new LinearLayout(this);controls.setOrientation(0);raiz.addView(controls);
    TextView prev=action("‹ Anterior",ink),speak=action("♫ Escuchar",coral),next=action("Siguiente ›",ink);
    for(TextView t:new TextView[]{prev,speak,next}){
      LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(0,-2,1);p.setMargins(dp(2),0,dp(2),0);controls.addView(t,p);
    }
    prev.setOnClickListener(v->{if(paso>0){paso--;actualizar();hablar();}});
    speak.setOnClickListener(v->hablar());
    next.setOnClickListener(v->{if(paso<5){paso++;actualizar();hablar();}else menu();});
    actualizar();
  }
  void actualizar(){titulo.setText(nombres[elegido]+"   ·   "+(paso+1)+" / 6");instruccion.setText(frases[elegido][paso]);dibujo.invalidate();}
  void hablar(){if(voz!=null&&elegido>=0)voz.speak(frases[elegido][paso],TextToSpeech.QUEUE_FLUSH,null,"paso");}
  @Override public void onBackPressed(){if(elegido>=0)menu();else super.onBackPressed();}
  @Override protected void onDestroy(){if(voz!=null){voz.stop();voz.shutdown();}super.onDestroy();}
  class Drawing extends View {
    Paint p=new Paint(3);
    int subject=-1;boolean preview=false;
    Drawing(Context c){this(c,-1,false);}
    Drawing(Context c,int index,boolean mini){
      super(c);subject=index;preview=mini;setLayerType(View.LAYER_TYPE_SOFTWARE,null);
      p.setColor(ink);p.setStrokeWidth(4.5f);p.setStyle(Paint.Style.STROKE);
      p.setStrokeCap(Paint.Cap.ROUND);p.setStrokeJoin(Paint.Join.ROUND);
    }
    void line(Canvas c,float... a){Path q=new Path();q.moveTo(a[0],a[1]);for(int i=2;i<a.length;i+=2)q.lineTo(a[i],a[i+1]);c.drawPath(q,p);}
    void oval(Canvas c,float a,float b,float d,float e){c.drawOval(a,b,d,e,p);}
    void circle(Canvas c,float x,float y,float r){c.drawCircle(x,y,r,p);}
    void rect(Canvas c,float a,float b,float d,float e){c.drawRect(a,b,d,e,p);}
    void drawStep(Canvas c,int n,int s){
      switch(n){
      case 0:
        oval(c,65,110,295,240);if(s>=1)line(c,290,175,375,110,375,240,290,175);if(s>=2)circle(c,118,151,8);if(s>=3)line(c,72,182,100,192);if(s>=4){line(c,165,112,200,72,236,113);line(c,165,239,204,274,239,235);}if(s>=5){line(c,322,150,357,138);line(c,325,175,362,175);line(c,322,199,357,211);}break;
      case 1:
        circle(c,200,128,76);if(s>=1){line(c,138,86,125,24,178,56);line(c,222,56,275,24,262,86);}if(s>=2){circle(c,173,119,9);circle(c,227,119,9);}if(s>=3){line(c,188,150,200,161,212,150);line(c,200,161,185,177);line(c,200,161,215,177);}if(s>=4){line(c,153,157,95,147);line(c,153,171,95,177);line(c,247,157,305,147);line(c,247,171,305,177);}if(s>=5){oval(c,154,205,246,295);line(c,243,265,315,284,324,241);}break;
      case 2:
        circle(c,200,115,67);if(s>=1){oval(c,113,72,150,163);oval(c,250,72,287,163);}if(s>=2){circle(c,175,112,7);circle(c,225,112,7);}if(s>=3){circle(c,200,142,11);line(c,200,153,185,169,215,169,200,153);}if(s>=4)oval(c,145,190,255,286);if(s>=5){line(c,160,270,155,302);line(c,190,275,190,305);line(c,220,275,220,305);line(c,245,270,250,302);line(c,253,230,305,200);}break;
      case 3:
        oval(c,190,90,210,260);if(s>=1){oval(c,88,74,194,185);oval(c,206,74,312,185);}if(s>=2){oval(c,113,174,195,272);oval(c,205,174,287,272);}if(s>=3)circle(c,200,79,18);if(s>=4){line(c,192,63,174,39);line(c,208,63,226,39);}if(s>=5){circle(c,146,132,20);circle(c,254,132,20);circle(c,153,222,15);circle(c,247,222,15);}break;
      case 4:
        oval(c,98,132,286,228);if(s>=1){line(c,252,146,276,80,316,75,330,123,287,167);}if(s>=2){line(c,293,80,290,58,307,76);line(c,317,79,333,61,328,94);circle(c,315,104,4);}if(s>=3){line(c,125,210,120,294);line(c,157,214,157,294);line(c,246,210,244,294);line(c,274,205,277,294);}if(s>=4)line(c,98,158,67,115,58,150);if(s>=5){line(c,273,86,257,120,265,154);line(c,115,293,135,293);line(c,236,293,254,293);}break;
      case 5:
        line(c,175,295,178,150);if(s>=1)line(c,225,295,222,150,175,150);if(s>=2)oval(c,92,37,308,198);if(s>=3){line(c,200,208,150,122);line(c,200,190,255,115);}if(s>=4){circle(c,143,85,14);circle(c,250,82,14);circle(c,200,59,13);}if(s>=5)line(c,45,296,350,296);break;
      case 6:
        circle(c,200,112,27);if(s>=1)for(int i=0;i<8;i++){double a=i*Math.PI/4;circle(c,200+(float)Math.cos(a)*54,112+(float)Math.sin(a)*54,29);}if(s>=2)line(c,200,169,200,292);if(s>=3)line(c,200,238,146,204,200,258);if(s>=4)line(c,200,253,254,213,200,274);if(s>=5)line(c,80,294,320,294);break;
      case 7:
        line(c,30,290,370,290);if(s>=1)line(c,45,290,145,90,248,290);if(s>=2)line(c,165,290,280,60,370,290);if(s>=3){line(c,112,155,145,90,177,155);line(c,246,130,280,60,312,131);}if(s>=4)circle(c,65,62,32);if(s>=5){oval(c,290,30,348,54);oval(c,310,19,370,50);}break;
      case 8:
        line(c,20,210,380,210);if(s>=1){line(c,60,210,140,90,220,210);line(c,175,210,285,72,370,210);}if(s>=2)circle(c,70,57,28);if(s>=3){line(c,100,285,100,190);circle(c,100,157,39);}if(s>=4)line(c,185,310,220,210,255,310);if(s>=5){oval(c,275,32,350,58);line(c,30,290,80,290);}break;
      case 9:
        rect(c,100,130,300,290);if(s>=1)line(c,80,132,200,37,320,132);if(s>=2)rect(c,175,207,225,290);if(s>=3){rect(c,117,164,159,205);rect(c,245,164,285,205);}if(s>=4)rect(c,258,53,283,100);if(s>=5)line(c,20,292,380,292);break;
      case 10:
        rect(c,55,150,345,246);if(s>=1)line(c,112,150,154,99,267,99,309,150);if(s>=2){circle(c,125,246,32);circle(c,277,246,32);}if(s>=3){rect(c,162,108,206,145);rect(c,213,108,260,145);}if(s>=4){rect(c,56,180,79,201);rect(c,320,180,344,201);}if(s>=5){line(c,208,153,208,239);line(c,30,281,370,281);}break;
      case 11:
        circle(c,103,227,58);circle(c,299,227,58);if(s>=1)line(c,103,227,167,140,218,227,103,227,218,227,299,227,253,142,167,140);if(s>=2)line(c,253,142,272,103,310,103);if(s>=3)line(c,167,140,161,119,198,119);if(s>=4){circle(c,218,227,12);line(c,218,227,236,242);}if(s>=5){for(int i=0;i<8;i++){double a=i*Math.PI/4;line(c,103,227,103+(float)Math.cos(a)*55,227+(float)Math.sin(a)*55);line(c,299,227,299+(float)Math.cos(a)*55,227+(float)Math.sin(a)*55);}}break;
      case 12:
        oval(c,113,38,287,281);if(s>=1){circle(c,165,139,10);circle(c,235,139,10);}if(s>=2)line(c,200,151,187,192,213,192);if(s>=3)line(c,165,221,200,239,235,221);if(s>=4){oval(c,96,154,115,190);oval(c,285,154,304,190);}if(s>=5)line(c,118,95,151,49,200,39,249,49,282,95);break;
      case 13:
        circle(c,200,65,34);if(s>=1)line(c,200,99,200,218);if(s>=2){line(c,200,126,123,182);line(c,200,126,277,182);}if(s>=3){line(c,200,218,141,298);line(c,200,218,259,298);}if(s>=4){circle(c,123,182,9);circle(c,277,182,9);oval(c,127,294,157,308);oval(c,244,294,274,308);}if(s>=5){circle(c,188,61,3);circle(c,212,61,3);line(c,190,78,200,83,210,78);}break;
      case 14:
        rect(c,145,37,255,139);if(s>=1)rect(c,125,142,275,250);if(s>=2){line(c,125,167,72,187,72,242);line(c,275,167,328,187,328,242);}if(s>=3){line(c,161,250,161,307);line(c,239,250,239,307);}if(s>=4){circle(c,175,83,12);circle(c,225,83,12);line(c,175,112,225,112);}if(s>=5){circle(c,180,190,10);circle(c,220,190,10);line(c,200,37,200,13);circle(c,200,12,5);}break;
      }
    }
    @Override protected void onDraw(Canvas c){
      super.onDraw(c);int index=subject>=0?subject:elegido;if(index<0)return;
      int save=c.save();float scale=Math.min(getWidth()/430f,getHeight()/350f);
      c.translate((getWidth()-400*scale)/2,(getHeight()-330*scale)/2);c.scale(scale,scale);
      p.setStrokeWidth(preview?6:4.5f);
      if(preview){p.setColor(ink);drawStep(c,index,5);}
      else {
        if(paso>0){p.setColor(Color.rgb(195,203,210));drawStep(c,index,paso-1);}
        p.setColor(coral);drawStep(c,index,paso);
      }
      c.restoreToCount(save);
    }
  }
}
