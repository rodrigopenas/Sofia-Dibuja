package com.sofiadibuja.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.graphics.*;
import android.content.Context;
import android.view.*;
import android.widget.*;
import java.util.*;

public class MainActivity extends Activity {
  TextToSpeech voz;
  int paso=0, elegido=-1, dificultad=0;
  float volumenVoz=0.55f, tonoVoz=0.85f;
  android.content.SharedPreferences prefs;
  LinearLayout raiz;
  TextView titulo, instruccion;
  Drawing dibujo;
  final String[] nombres={"Pez","Gato","Perro","Mariposa","Caballo","Árbol","Flor","Montañas","Paisaje","Casa","Auto","Bicicleta","Cara","Persona","Robot","Cohete","Castillo","Dinosaurio","Búho","Tortuga","Conejo","Elefante","Ballena","Pulpo","Barco","Avión","Tren","Faro","Hongos","Helado","Dragón"};
  final String[] categorias={"Animales","Animales","Animales","Animales","Animales","Naturaleza","Naturaleza","Naturaleza","Naturaleza","Objetos","Objetos","Objetos","Personajes","Personajes","Personajes","Aventura","Aventura","Aventura","Animales nuevos","Animales nuevos","Animales nuevos","Animales nuevos","Animales nuevos","Animales nuevos","Transportes","Transportes","Transportes","Paisajes nuevos","Paisajes nuevos","Objetos nuevos","Fantasía"};
  final String[][] frases={
    {"Dibujá un óvalo grande en el centro de la hoja.","Agregá una cola triangular a la derecha.","Poné un ojo redondo a la izquierda.","Dibujá una boca pequeña.","Sumá una aleta arriba y otra abajo.","Terminá con líneas en la cola. ¡Muy bien!"},
    {"Dibujá un círculo grande para la cabeza.","Agregá dos orejas triangulares.","Dibujá dos ojos redondos.","Poné una nariz triangular y una boca.","Sumá bigotes a los costados.","Dibujá el cuerpo y una cola curva."},
    {"Dibujá un círculo para la cabeza.","Agregá dos orejas caídas.","Dibujá dos ojos.","Poné una nariz y una boca.","Dibujá un cuerpo ovalado.","Agregá cuatro patas y una cola."},
    {"Dibujá un cuerpo largo y finito en el centro.","Sumá las dos alas superiores.","Agregá las dos alas inferiores.","Dibujá una cabeza redonda.","Poné dos antenas.","Decorá las alas con círculos."},
    {"Dibujá un óvalo grande para el cuerpo.","Agregá el cuello y la cabeza.","Dibujá dos orejas y un ojo.","Sumá las cuatro patas.","Dibujá una cola larga.","Agregá la crin y los cascos."},
    {"Dibujá la primera línea vertical del tronco.","Dibujá la segunda línea vertical y unilas arriba para formar el tronco.","Agregá una copa grande y redonda.","Dibujá dos ramas dentro de la copa.","Sumá hojas con círculos pequeños.","Dibujá el suelo debajo del árbol."},
    {"Dibujá un círculo para el centro de la flor.","Agregá pétalos alrededor.","Dibujá un tallo hacia abajo.","Sumá una hoja a la izquierda.","Agregá otra hoja a la derecha.","Dibujá pastito en la base."},
    {"Dibujá una línea horizontal para el suelo.","Dibujá una montaña triangular.","Agregá otra montaña al lado.","Dibujá nieve en las cumbres.","Sumá un sol redondo.","Agregá algunas nubes."},
    {"Dibujá una línea de horizonte.","Agregá dos montañas al fondo.","Dibujá un sol arriba.","Sumá un árbol a la izquierda.","Dibujá un camino curvo.","Agregá nubes y pastito."},
    {"Dibujá un cuadrado grande.","Agregá un techo triangular.","Dibujá una puerta rectangular.","Sumá dos ventanas.","Agregá una chimenea.","Dibujá un camino y el suelo."},
    {"Dibujá un rectángulo largo para el auto.","Agregá el techo con dos líneas.","Dibujá dos ruedas redondas.","Sumá dos ventanas.","Dibujá las luces.","Agregá una puerta y el suelo."},
    {"Dibujá dos ruedas grandes.","Uní las ruedas con el cuadro triangular.","Agregá el manubrio.","Dibujá el asiento.","Sumá los pedales.","Agregá los rayos de las ruedas."},
    {"Dibujá un óvalo para la cara.","Agregá dos ojos.","Dibujá una nariz pequeña.","Sumá una boca sonriente.","Agregá dos orejas.","Dibujá el pelo."},
    {"Dibujá un círculo para la cabeza.","Agregá una línea para el cuerpo.","Dibujá dos brazos.","Sumá dos piernas.","Agregá manos y pies.","Dibujá ojos, nariz y boca."},
    {"Dibujá un cuadrado para la cabeza del robot.","Agregá un rectángulo para el cuerpo.","Dibujá dos brazos.","Sumá dos piernas.","Agregá ojos y una boca.","Dibujá botones y una antena."},
    {"Dibujá un rectángulo alto para el cuerpo del cohete.","Agregá una punta triangular.","Dibujá dos ventanas circulares.","Sumá dos aletas laterales.","Dibujá las llamas de los motores.","Agregá estrellas y un planeta de fondo."},
    {"Dibujá un rectángulo ancho para la muralla del castillo.","Agregá dos torres altas a los costados.","Dibujá los techos puntiagudos de las torres.","Sumá una puerta grande con arco.","Agregá ventanas y almenas.","Dibujá piedras, un camino y una bandera."},
    {"Dibujá un óvalo grande para el cuerpo del dinosaurio.","Agregá un cuello largo que termine en una cabeza.","Dibujá una cola larga y curva.","Sumá cuatro patas fuertes.","Agregá un ojo y una boca sonriente.","Dibujá manchas, pasto y montañas de fondo."},
    {"Dibujá un óvalo vertical para el cuerpo del búho.","Agregá dos orejas puntiagudas.","Dibujá dos ojos grandes.","Sumá un pico triangular.","Agregá las alas a los costados.","Dibujá plumas y una rama donde se posa."},
    {"Dibujá un óvalo ancho para el caparazón.","Agregá la cabeza redonda a la derecha.","Dibujá cuatro patas cortas.","Sumá una cola triangular.","Agregá un ojo y una sonrisa.","Decorá el caparazón con figuras."},
    {"Dibujá un óvalo para la cabeza del conejo.","Agregá dos orejas largas.","Dibujá los ojos y la nariz.","Sumá un cuerpo redondeado.","Dibujá las patas y una cola.","Agregá bigotes y pastito."},
    {"Dibujá un óvalo grande para el cuerpo del elefante.","Agregá una cabeza redonda a la derecha.","Dibujá una trompa larga y curva.","Sumá dos orejas grandes.","Dibujá las cuatro patas.","Agregá ojos, cola y arrugas."},
    {"Dibujá un óvalo grande y horizontal para la ballena.","Agregá la cola de dos puntas.","Dibujá un ojo pequeño.","Sumá una aleta a un costado.","Dibujá un chorro de agua.","Agregá olas y detalles en el cuerpo."},
    {"Dibujá un óvalo para la cabeza del pulpo.","Agregá dos tentáculos hacia abajo.","Dibujá otros dos tentáculos curvos.","Sumá cuatro tentáculos más.","Dibujá los ojos y una boca.","Agregá ventosas y burbujas."},
    {"Dibujá el casco largo del barco.","Agregá un mástil vertical.","Dibujá una vela triangular a la izquierda.","Sumá otra vela a la derecha.","Agregá una bandera arriba.","Dibujá olas, nubes y detalles."},
    {"Dibujá un cuerpo largo para el avión.","Agregá las alas extendidas.","Dibujá la cola con su aleta.","Sumá las ventanas.","Dibujá dos motores.","Agregá nubes y detalles del fuselaje."},
    {"Dibujá un rectángulo grande para la locomotora.","Agregá una cabina alta.","Dibujá tres ruedas grandes.","Sumá una chimenea.","Agregá ventanas y luces.","Dibujá humo, rieles y detalles."},
    {"Dibujá una torre alta para el faro.","Agregá el techo triangular.","Dibujá una ventana arriba.","Sumá la puerta en la base.","Dibujá rayos de luz a los lados.","Agregá rocas y olas."},
    {"Dibujá un tallo para el primer hongo.","Agregá un sombrero grande arriba.","Dibujá un segundo hongo pequeño.","Sumá un tercer hongo.","Agregá manchas en los sombreros.","Dibujá pasto y piedras."},
    {"Dibujá un triángulo invertido para el cucurucho.","Agregá una bocha grande de helado.","Dibujá otra bocha encima.","Sumá una cereza arriba.","Decorá el cucurucho con líneas.","Agregá chispas y detalles."},
    {"Dibujá un óvalo largo para el cuerpo del dragón.","Agregá un cuello y una cabeza alargada.","Dibujá una cola curva.","Sumá cuatro patas con garras.","Agregá alas grandes.","Dibujá cuernos, escamas y humo."}
  };
  @Override public void onCreate(Bundle b) {
    super.onCreate(b);
    prefs=getSharedPreferences("sofia_config",MODE_PRIVATE);
    volumenVoz=prefs.getFloat("volumen",0.55f);
    tonoVoz=prefs.getFloat("tono",0.85f);
    voz=new TextToSpeech(this,s->{if(s==TextToSpeech.SUCCESS){voz.setLanguage(new Locale("es","AR"));voz.setSpeechRate(0.90f);voz.setPitch(tonoVoz);}});
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
    TextView settings=action("♫ Ajustar voz y volumen",ink);raiz.addView(settings);
    settings.setOnClickListener(v->ajustarVoz());
    TextView levelLabel=text("Dificultad",17,ink);levelLabel.setPadding(0,dp(10),0,0);raiz.addView(levelLabel);
    Spinner levels=new Spinner(this);
    final ArrayList<Drawing> previews=new ArrayList<>();
    String[] opciones={"Inicial · figuras simples","Intermedio · más detalles","Avanzado · dibujo expresivo"};
    levels.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,opciones));
    levels.setSelection(dificultad);
    levels.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener(){
      public void onNothingSelected(android.widget.AdapterView<?> parent){}
      public void onItemSelected(android.widget.AdapterView<?> parent,View view,int position,long id){dificultad=position;for(Drawing item:previews)item.invalidate();}
    });
    raiz.addView(levels);
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
      Drawing thumb=new Drawing(this,index,true);previews.add(thumb);card.addView(thumb,new LinearLayout.LayoutParams(-1,dp(105)));
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
    TextView hint=text("Mirá el ejemplo y dibujalo en tu papel · "+new String[]{"Inicial","Intermedio","Avanzado"}[dificultad],14,coral);
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
  String fraseActual(){
    String base=frases[elegido][paso];
    if(dificultad==0)return base;
    String[] intermedio={
      "Prestá atención a las proporciones y dejá espacio para agregar detalles.",
      "Buscá que las líneas se unan suavemente, sin apretar mucho el lápiz.",
      "Compará tamaños y posiciones antes de marcar los trazos.",
      "Agregá líneas curvas para dar una apariencia más natural.",
      "Probá sumar pequeños detalles que hagan reconocible el dibujo.",
      "Completá los detalles de la figura y repasá los trazos principales."
    };
    String[] avanzado={
      "Empezá con trazos suaves de construcción y cuidá la composición.",
      "Pensá en el volumen y la perspectiva mientras completás la forma.",
      "Buscá simetría o asimetría natural según el objeto.",
      "Variá la presión del lápiz para diferenciar contornos y detalles.",
      "Definí una dirección de luz y reservá las zonas iluminadas.",
      "Completá los detalles y sumá líneas finas de sombra en un costado."
    };
    return base+" "+(dificultad==1?intermedio[paso]:avanzado[paso]);
  }
  void actualizar(){titulo.setText(nombres[elegido]+"   ·   "+(paso+1)+" / 6");instruccion.setText(fraseActual());dibujo.invalidate();}
  void hablar(){
    if(voz!=null&&elegido>=0){
      Bundle opciones=new Bundle();
      opciones.putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME,volumenVoz);
      voz.speak(fraseActual(),TextToSpeech.QUEUE_FLUSH,opciones,"paso");
    }
  }
  void ajustarVoz(){
    LinearLayout panel=new LinearLayout(this);panel.setOrientation(1);panel.setPadding(dp(20),dp(12),dp(20),dp(8));
    TextView info=text("Ajustá la voz para que resulte suave y agradable. El volumen de esta app es independiente del volumen general.",16,ink);panel.addView(info);
    TextView vl=text("Volumen de la voz",17,ink);panel.addView(vl);
    SeekBar vol=new SeekBar(this);vol.setMax(100);vol.setProgress(Math.round(volumenVoz*100));panel.addView(vol);
    TextView pitch=text("Tono de la voz",17,ink);panel.addView(pitch);
    SeekBar tono=new SeekBar(this);tono.setMax(100);tono.setProgress(Math.round((tonoVoz-0.65f)*100/0.55f));panel.addView(tono);
    new AlertDialog.Builder(this).setTitle("Configurar voz").setView(panel)
      .setPositiveButton("Guardar",(d,w)->{
        volumenVoz=Math.max(0.1f,vol.getProgress()/100f);
        tonoVoz=0.65f+tono.getProgress()*0.55f/100f;
        prefs.edit().putFloat("volumen",volumenVoz).putFloat("tono",tonoVoz).apply();
        if(voz!=null)voz.setPitch(tonoVoz);
      }).setNegativeButton("Cancelar",null).show();
  }
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
    void detallesNivel(Canvas c,int n,int s){
      if(dificultad==0 || s<2)return;
      int old=p.getColor();float oldWidth=p.getStrokeWidth();
      p.setColor(preview?ink:coral);p.setStrokeWidth(3.1f);
      // Las partes adicionales se incorporan en pasos concretos, no sólo al final.
      switch(n){
        case 0: // Pez: escamas, branquias, burbujas y agua
          if(s>=2){line(c,142,133,132,172,143,208);circle(c,105,153,3);}
          if(s>=3)for(int i=0;i<3;i++)for(int j=0;j<2;j++)line(c,175+i*32,145+j*34,187+i*32,154+j*34,175+i*32,163+j*34);
          if(s>=4){circle(c,70,85,10);circle(c,51,57,6);}
          if(s>=5){line(c,35,288,90,280,150,288,215,280,270,288,360,280);if(dificultad==2)for(int i=0;i<5;i++)line(c,130+i*30,215,143+i*30,225);}
          break;
        case 1: // Gato: pelaje, patas, manchas
          if(s>=2){line(c,157,88,169,79);line(c,232,80,244,89);}
          if(s>=3){line(c,193,90,200,101,207,90);line(c,185,97,192,105);}
          if(s>=4){line(c,171,261,171,292);line(c,228,261,228,292);}
          if(s>=5){for(int i=0;i<3;i++){line(c,169+i*15,279,169+i*15,289);line(c,216+i*12,279,216+i*12,289);}if(dificultad==2){line(c,155,217,179,230);line(c,244,218,221,231);}}
          break;
        case 2: // Perro: manchas, collar y pelaje
          if(s>=2){oval(c,157,96,190,126);oval(c,211,96,242,126);}
          if(s>=3){line(c,163,176,237,176);circle(c,200,181,7);}
          if(s>=4){oval(c,171,210,201,244);}
          if(s>=5){line(c,152,287,168,287);line(c,235,287,251,287);if(dificultad==2){line(c,270,237,299,222);line(c,270,245,302,235);}}
          break;
        case 3: // Mariposa: nervaduras, simetría y puntos
          if(s>=2){line(c,195,142,125,106);line(c,205,142,275,106);}
          if(s>=3){line(c,195,207,135,234);line(c,205,207,265,234);}
          if(s>=4){circle(c,162,125,9);circle(c,238,125,9);}
          if(s>=5){for(int i=0;i<3;i++){circle(c,145+i*13,161,3);circle(c,229+i*13,161,3);}if(dificultad==2){line(c,195,180,121,167);line(c,205,180,279,167);}}
          break;
        case 4: // Caballo: crin, músculos, detalles
          if(s>=2){line(c,285,91,266,102,280,109);line(c,281,111,260,123);}
          if(s>=3){line(c,119,180,151,193);line(c,239,190,269,185);}
          if(s>=4){line(c,90,158,69,139,58,143);line(c,107,162,84,138);}
          if(s>=5){line(c,277,117,287,143);line(c,279,125,292,152);if(dificultad==2){line(c,126,209,135,231);line(c,248,212,256,230);}}
          break;
        case 5: // Árbol: ramas y hojas orgánicas
          if(s>=2){line(c,178,181,128,127);line(c,223,178,273,126);}
          if(s>=3){line(c,178,204,146,163);line(c,221,205,259,164);}
          if(s>=4)for(int i=0;i<6;i++)circle(c,126+(i%3)*68,78+(i/3)*74,10);
          if(s>=5){line(c,173,268,149,282);line(c,227,270,247,286);if(dificultad==2)for(int i=0;i<6;i++)line(c,110+i*29,215,100+i*29,229);}
          break;
        case 6: // Flor: pétalos internos, nervaduras
          if(s>=2)for(int i=0;i<8;i++){double a=i*Math.PI/4;line(c,200+(float)Math.cos(a)*32,112+(float)Math.sin(a)*32,200+(float)Math.cos(a)*59,112+(float)Math.sin(a)*59);}
          if(s>=3)line(c,200,241,160,213);
          if(s>=4)line(c,200,260,241,221);
          if(s>=5){for(int i=0;i<7;i++)circle(c,185+(i%3)*10,100+(i/3)*10,2);if(dificultad==2){line(c,185,220,162,213);line(c,216,238,238,221);}}
          break;
        case 7: // Montañas: cordillera y sombras
          if(s>=2){line(c,80,260,145,165,204,263);line(c,215,251,280,136,333,251);}
          if(s>=3){line(c,145,90,173,167);line(c,280,60,304,140);}
          if(s>=4){line(c,40,294,78,272,114,294);}
          if(s>=5){line(c,170,291,218,270,264,291);if(dificultad==2)for(int i=0;i<5;i++)line(c,245+i*12,175,227+i*12,205);}
          break;
        case 8: // Paisaje: perspectiva y textura
          if(s>=2){line(c,36,211,70,193);line(c,326,211,365,188);}
          if(s>=3){line(c,99,191,71,215);line(c,100,193,129,218);}
          if(s>=4){line(c,199,308,220,233);line(c,239,308,220,233);}
          if(s>=5){for(int i=0;i<6;i++)line(c,55+i*49,289,62+i*49,283);if(dificultad==2){line(c,20,248,65,239,104,251);line(c,271,252,329,238,380,251);}}
          break;
        case 9: // Casa: tejas, ladrillos, jardín
          if(s>=2){line(c,110,123,200,55,290,123);line(c,183,234,190,234);}
          if(s>=3){line(c,117,185,159,185);line(c,245,185,285,185);}
          if(s>=4){for(int i=0;i<5;i++)line(c,134+i*30,88,148+i*30,101);}
          if(s>=5){rect(c,72,225,96,258);circle(c,331,244,21);if(dificultad==2){for(int i=0;i<5;i++)line(c,113+i*35,143,125+i*35,143);}}
          break;
        case 10: // Auto: ruedas y carrocería detalladas
          if(s>=2){circle(c,125,246,16);circle(c,277,246,16);}
          if(s>=3){line(c,210,108,210,144);line(c,158,102,146,141);}
          if(s>=4){line(c,82,206,116,206);line(c,288,206,318,206);}
          if(s>=5){line(c,76,228,100,228);line(c,299,228,329,228);if(dificultad==2){line(c,93,165,305,165);line(c,188,176,226,176);}}
          break;
        case 11: // Bicicleta: cadena, rayos y engranajes
          if(s>=2){circle(c,103,227,11);circle(c,299,227,11);}
          if(s>=3){line(c,161,119,157,111);line(c,198,119,204,112);}
          if(s>=4){circle(c,218,227,22);line(c,217,247,168,247,103,237);}
          if(s>=5){line(c,212,210,229,196);if(dificultad==2){circle(c,218,227,6);line(c,242,147,261,161);}}
          break;
        case 12: // Cara: cejas, pestañas, sombreado
          if(s>=2){line(c,150,111,177,104);line(c,221,104,248,111);}
          if(s>=3){line(c,168,223,200,250,232,223);}
          if(s>=4){line(c,127,179,151,183);line(c,249,183,273,179);}
          if(s>=5){for(int i=0;i<3;i++){line(c,158+i*9,126,155+i*9,120);line(c,223+i*9,126,220+i*9,120);}if(dificultad==2){line(c,144,197,168,204);line(c,232,204,256,197);}}
          break;
        case 13: // Persona: ropa, articulaciones, accesorios
          if(s>=2){line(c,173,119,200,146,227,119);}
          if(s>=3){line(c,177,213,223,213);line(c,175,220,225,220);}
          if(s>=4){circle(c,123,182,4);circle(c,277,182,4);}
          if(s>=5){line(c,192,165,208,165);line(c,192,181,208,181);if(dificultad==2){line(c,155,170,171,185);line(c,245,170,229,185);}}
          break;
        case 14: // Robot: paneles, cables y mecanismos
          if(s>=2){rect(c,142,170,258,211);}
          if(s>=3){circle(c,72,242,8);circle(c,328,242,8);}
          if(s>=4){rect(c,167,56,233,107);line(c,175,112,225,112);}
          if(s>=5){for(int i=0;i<4;i++)line(c,153+i*28,235,165+i*28,235);if(dificultad==2){line(c,149,142,166,153);line(c,251,142,234,153);}}
          break;
        case 15: // Cohete: placas, motores y espacio
          if(s>=2){line(c,158,115,242,115);line(c,158,230,242,230);}
          if(s>=3){line(c,135,239,158,220);line(c,242,220,265,239);}
          if(s>=4){line(c,184,251,175,283);line(c,216,251,225,283);}
          if(s>=5){circle(c,333,118,11);circle(c,70,218,8);if(dificultad==2){for(int i=0;i<5;i++)line(c,165+i*15,174,174+i*15,181);}}
          break;
        case 16: // Castillo: piedras, ventanas, almenas
          if(s>=2){line(c,125,144,275,144);line(c,125,165,275,165);}
          if(s>=3){rect(c,180,172,220,190);line(c,200,203,200,291);}
          if(s>=4){for(int i=0;i<5;i++)rect(c,131+i*28,121,144+i*28,139);}
          if(s>=5){for(int i=0;i<5;i++)line(c,133+i*30,205,146+i*30,205);if(dificultad==2){for(int i=0;i<4;i++)line(c,132+i*40,243,147+i*40,243);}}
          break;
        case 17: // Dinosaurio: espinas, textura y paisaje
          if(s>=2){for(int i=0;i<5;i++)line(c,105+i*33,145,120+i*33,119,138+i*33,147);}
          if(s>=3){line(c,146,246,170,233);line(c,238,245,261,232);}
          if(s>=4){line(c,302,112,306,120);circle(c,290,96,3);}
          if(s>=5){for(int i=0;i<6;i++)circle(c,135+i*23,186+(i%2)*19,6);if(dificultad==2){line(c,45,286,75,270,95,286);line(c,280,290,317,263,344,290);}}
          break;
      }
      if(dificultad==2 && s>=3){p.setColor(Color.rgb(110,129,149));p.setStrokeWidth(1.7f);for(int i=0;i<8;i++)line(c,265+i*7,267,251+i*7,291);}
      p.setColor(old);p.setStrokeWidth(oldWidth);
    }
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
      case 15:
        rect(c,157,100,243,248);if(s>=1)line(c,157,100,200,30,243,100);if(s>=2){circle(c,200,145,20);circle(c,200,207,14);}if(s>=3){line(c,157,188,120,246,157,246);line(c,243,188,280,246,243,246);}if(s>=4){line(c,173,248,163,304,200,273,237,304,227,248);}if(s>=5){circle(c,75,75,18);circle(c,315,62,22);line(c,300,168,310,183,320,168);}break;
      case 16:
        rect(c,100,132,300,292);if(s>=1){rect(c,60,91,114,292);rect(c,286,91,340,292);}if(s>=2){line(c,55,91,87,38,119,91);line(c,281,91,313,38,345,91);}if(s>=3){line(c,169,292,169,235,200,203,231,235,231,292);}if(s>=4){rect(c,74,143,100,177);rect(c,300,143,326,177);line(c,120,132,120,111,143,111,143,132);line(c,257,132,257,111,280,111,280,132);}if(s>=5){line(c,313,38,313,10,347,21,313,31);line(c,30,307,370,307);line(c,135,165,157,165);}break;
      case 17:
        oval(c,95,146,286,247);if(s>=1){line(c,243,159,265,77,312,63,335,89,323,112,278,111);}if(s>=2){line(c,98,177,48,132,20,161,94,223);}if(s>=3){line(c,132,233,127,298,154,298,167,241);line(c,230,235,227,298,254,298,265,229);}if(s>=4){circle(c,309,85,5);line(c,315,105,327,108);}if(s>=5){circle(c,158,180,12);circle(c,212,204,15);line(c,35,307,365,307);line(c,35,108,95,73,145,108);}break;
      case 18: oval(c,125,70,275,285);if(s>=1){line(c,139,99,123,44,168,78);line(c,232,78,277,44,261,99);}if(s>=2){circle(c,167,146,34);circle(c,233,146,34);}if(s>=3)line(c,188,184,200,207,212,184,188,184);if(s>=4){oval(c,118,174,149,247);oval(c,251,174,282,247);}if(s>=5){for(int i=0;i<5;i++)line(c,160+i*19,238,166+i*19,253);line(c,70,291,330,291);}break;
      case 19: oval(c,80,122,285,262);if(s>=1)circle(c,318,172,40);if(s>=2){oval(c,120,235,156,288);oval(c,238,235,274,288);}if(s>=3){oval(c,110,112,150,154);line(c,80,199,53,218,83,230);}if(s>=4){circle(c,326,164,5);line(c,320,188,338,188);}if(s>=5){for(int i=0;i<3;i++)line(c,132+i*49,140,160+i*49,183,130+i*49,225);line(c,35,303,365,303);}break;
      case 20: oval(c,116,71,284,235);if(s>=1){oval(c,143,8,178,115);oval(c,222,8,257,115);}if(s>=2){circle(c,170,149,8);circle(c,230,149,8);circle(c,200,180,8);}if(s>=3)oval(c,130,209,271,295);if(s>=4){oval(c,140,272,182,304);oval(c,225,272,267,304);circle(c,273,259,22);}if(s>=5){line(c,184,181,145,190);line(c,216,181,255,190);line(c,35,312,365,312);}break;
      case 21: oval(c,62,112,278,250);if(s>=1)circle(c,289,153,62);if(s>=2)line(c,329,175,346,224,325,260,302,247);if(s>=3){oval(c,237,124,271,214);oval(c,323,124,357,214);}if(s>=4){for(int i=0;i<4;i++)rect(c,95+i*47,235,123+i*47,304);}if(s>=5){circle(c,299,140,5);line(c,62,166,34,139,29,185);line(c,35,310,366,310);}break;
      case 22: oval(c,57,119,315,248);if(s>=1)line(c,300,165,364,117,354,176,367,221,302,207);if(s>=2)circle(c,99,166,6);if(s>=3)line(c,176,242,228,278,248,231);if(s>=4){line(c,179,120,177,80,163,63);line(c,179,80,195,62);}if(s>=5){line(c,30,290,95,278,158,290,222,278,286,290,365,278);line(c,117,195,183,208);}break;
      case 23: oval(c,120,42,280,172);if(s>=1){line(c,153,158,100,255,140,275);line(c,181,171,163,287,195,295);}if(s>=2){line(c,220,168,241,288,276,273);line(c,255,153,310,251,283,286);}if(s>=3){line(c,171,166,76,223,103,278);line(c,229,165,326,226,298,280);}if(s>=4){circle(c,173,103,11);circle(c,228,103,11);line(c,188,133,200,142,212,133);}if(s>=5){for(int i=0;i<6;i++)circle(c,111+i*32,245+(i%2)*12,5);circle(c,80,80,10);circle(c,316,66,9);}break;
      case 24: line(c,55,226,345,226,305,286,96,286,55,226);if(s>=1)line(c,202,226,202,63);if(s>=2)line(c,190,83,94,217,190,217,190,83);if(s>=3)line(c,215,91,302,217,215,217,215,91);if(s>=4)line(c,202,63,225,75,202,87);if(s>=5){line(c,35,301,105,292,175,302,245,292,365,301);circle(c,80,59,20);}break;
      case 25: oval(c,55,120,350,200);if(s>=1){line(c,137,120,180,67,248,120);line(c,145,200,181,252,239,200);}if(s>=2){line(c,305,136,339,92,349,160);line(c,315,179,338,224,351,160);}if(s>=3){for(int i=0;i<5;i++)circle(c,133+i*38,155,9);}if(s>=4){oval(c,140,207,196,244);oval(c,240,207,296,244);}if(s>=5){oval(c,47,44,120,70);oval(c,280,36,360,66);line(c,67,170,112,170);}break;
      case 26: rect(c,72,132,285,261);if(s>=1)rect(c,228,77,333,260);if(s>=2){circle(c,112,264,28);circle(c,205,264,28);circle(c,295,264,28);}if(s>=3)rect(c,115,91,161,134);if(s>=4)rect(c,170,49,212,132);if(s>=5){for(int i=0;i<3;i++)circle(c,185+i*30,31-i*6,13);line(c,32,307,365,307);line(c,65,318,344,318);}break;
      case 27: line(c,142,288,169,76,231,76,258,288,142,288);if(s>=1)line(c,164,76,200,27,236,76);if(s>=2)rect(c,176,97,224,139);if(s>=3)rect(c,185,244,215,288);if(s>=4){line(c,170,112,85,86);line(c,230,112,315,86);}if(s>=5){line(c,35,300,88,274,141,299);line(c,259,299,310,275,366,300);}break;
      case 28: rect(c,118,154,159,287);if(s>=1)oval(c,70,95,207,167);if(s>=2){rect(c,245,195,270,289);oval(c,214,165,301,205);}if(s>=3){rect(c,320,222,338,290);oval(c,295,195,365,232);}if(s>=4){circle(c,114,121,10);circle(c,158,132,12);circle(c,255,181,7);}if(s>=5)line(c,35,294,365,294);break;
      case 29: line(c,122,175,278,175,200,312,122,175);if(s>=1)circle(c,200,144,61);if(s>=2)circle(c,200,65,47);if(s>=3){line(c,200,20,200,9);circle(c,200,9,9);}if(s>=4){for(int i=0;i<6;i++)line(c,155+i*17,199,164+i*17,214);for(int i=0;i<5;i++)line(c,161+i*16,230,170+i*16,245);}if(s>=5){for(int i=0;i<7;i++)circle(c,160+i*14,142+(i%2)*13,4);}break;
      case 30: oval(c,88,133,293,237);if(s>=1)line(c,255,151,286,84,328,75,342,107,299,130);if(s>=2)line(c,90,175,32,113,22,172,86,222);if(s>=3){for(int i=0;i<4;i++)line(c,115+i*42,221,111+i*42,290,134+i*42,290);}if(s>=4){line(c,180,145,122,58,191,96,238,46,247,152);}if(s>=5){for(int i=0;i<5;i++)line(c,127+i*29,133,140+i*29,114,155+i*29,137);circle(c,313,96,4);}break;
      case 14:
        rect(c,145,37,255,139);if(s>=1)rect(c,125,142,275,250);if(s>=2){line(c,125,167,72,187,72,242);line(c,275,167,328,187,328,242);}if(s>=3){line(c,161,250,161,307);line(c,239,250,239,307);}if(s>=4){circle(c,175,83,12);circle(c,225,83,12);line(c,175,112,225,112);}if(s>=5){circle(c,180,190,10);circle(c,220,190,10);line(c,200,37,200,13);circle(c,200,12,5);}break;
      }
    }
    @Override protected void onDraw(Canvas c){
      super.onDraw(c);int index=subject>=0?subject:elegido;if(index<0)return;
      int save=c.save();float scale=Math.min(getWidth()/430f,getHeight()/350f);
      c.translate((getWidth()-400*scale)/2,(getHeight()-330*scale)/2);c.scale(scale,scale);
      p.setStrokeWidth(preview?6:4.5f);
      if(preview){p.setColor(ink);drawStep(c,index,5);detallesNivel(c,index,5);}
      else {
        if(paso>0){p.setColor(Color.rgb(195,203,210));drawStep(c,index,paso-1);}
        p.setColor(coral);drawStep(c,index,paso);detallesNivel(c,index,paso);
      }
      c.restoreToCount(save);
    }
  }
}
