package com.example.fatflat.ui.logged;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fatflat.R;
import com.example.fatflat.ui.ControladoraChat;
import com.example.fatflat.ui.ControladoraEditOffer;
import com.example.fatflat.ui.ControladoraOffer;
import com.example.fatflat.ui.ControladoraPresentacio;
import com.example.fatflat.ui.logged.weekview.ScheduleVisitActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VisualizeOffer extends AppCompatActivity {

    String[] categorias = new String[7];
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    final int[] numImages = {0};
    Uri image_uri;
    ImageView PreviewFoto0, PreviewFoto1, PreviewFoto2, PreviewFoto3, PreviewFoto4;
    ImageView[] PreviewFotos;
    final Integer[] cantidad_fotos = {0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualize_offer);
        //Escondemos la Action Bar porque usamos la ToolBar, aunque podriamos usar la ActionBar
        getSupportActionBar().hide();

        String folder_product = "/products/Q9KGzX0vB7rC5aakqBEp/";
        final StorageReference imagesRef = storageRef.child(folder_product);


        final ImageView Atras = findViewById(R.id.VisualizeOffer_Atras);
        final TextView accuracy = findViewById(R.id.VisualizeOffer_Accuracy_dinamic);
        final TextView venta = findViewById(R.id.VisualizeOffer_Venta_dinamic);
        final TextView tipo_inmueble = findViewById(R.id.VisualizeOffer_TipoInmueble_dinamic);
        final TextView poblacion = findViewById(R.id.VisualizeOffer_Poblacion_dinamic);
        final TextView zona = findViewById(R.id.VisualizeOffer_Zona_dinamic);
        final TextView precio = findViewById(R.id.VisualizeOffer_Precio_dinamic);
        final TextView superficie = findViewById(R.id.VisualizeOffer_Superficie_dinamic);
        final TextView numero_habitaciones = findViewById(R.id.VisualizeOffer_NHabitaciones_dinamic);
        final TextView numero_banyos = findViewById(R.id.VisualizeOffer_NBanyos_dinamic);
        final TextView fecha_publicacion = findViewById(R.id.VisualizeOffer_FechaPublicacion_dinamic);
        final TextView estado_inmueble = findViewById(R.id.VisualizeOffer_EstadoInmueble_dinamic);
        final TextView descriptionOffer = findViewById(R.id.textDescripcion_VisualizeOffer);
        final CheckBox piscina = findViewById(R.id.VisualizeOffer_ZonasAdds_Piscina);
        final CheckBox parking = findViewById(R.id.VisualizeOffer_ZonasAdds_Parking);
        final CheckBox terraza = findViewById(R.id.VisualizeOffer_ZonasAdds_Terraza);
        final CheckBox jardin = findViewById(R.id.VisualizeOffer_ZonasAdds_Jardin);
        final CheckBox trastero = findViewById(R.id.VisualizeOffer_ZonasAdds_Trastero);
        final CheckBox garaje = findViewById(R.id.VisualizeOffer_ZonasAdds_Garaje);
        final CheckBox ascensor = findViewById(R.id.VisualizeOffer_Extras_Ascensor);
        final CheckBox chimenea = findViewById(R.id.VisualizeOffer_Extras_Chimenea);
        final CheckBox calefaccion = findViewById(R.id.VisualizeOffer_Extras_Calefaccion);
        final CheckBox amueblado = findViewById(R.id.VisualizeOffer_Extras_Amueblado);
        final CheckBox aire_acondicionado = findViewById(R.id.VisualizeOffer_Extras_AireAcondicionado);
        //final ImageView afegirFavorite = findViewById(R.id.imageView_Favorite);
        //final ImageView new_chat = findViewById(R.id.icona_new_missatge);
        final ImageView Calendar = findViewById(R.id.VisualizeOffer_ScheduleVisit);
        final Button new_chat = findViewById(R.id.ok_button_VisualizeOffer);

        //Inicializamos los TextView con nuestros datos
        accuracy.setText(ControladoraOffer.getAccuracy() + "%");
        if (ControladoraOffer.isVenta()) venta.setText("venta");
        else venta.setText("alquiler");
        tipo_inmueble.setText(ControladoraOffer.getTipo_inmueble());
        poblacion.setText(ControladoraOffer.getPoblacion());
        zona.setText(ControladoraOffer.getZona());
        String s = String.valueOf(ControladoraOffer.getPrecio());
        s = s.substring(0,3) + "." + s.substring(3,6);
        precio.setText(s + " €");
        superficie.setText(String.valueOf(ControladoraOffer.getSuperficie()) + " m2");
        numero_habitaciones.setText(String.valueOf(ControladoraOffer.getNumero_habitaciones()));
        numero_banyos.setText(String.valueOf(ControladoraOffer.getNumero_banyos()));
        fecha_publicacion.setText(ControladoraOffer.getFecha_publicacion());
        estado_inmueble.setText(ControladoraOffer.getEstado_inmueble());
        descriptionOffer.setText(ControladoraPresentacio.getOffer_Description());
        //Inicializamos los CheckBox con nuestros datos
        CheckBox[] zonas_totales = {piscina, parking, terraza, jardin, trastero, garaje};
        boolean[] zonas_adds = ControladoraOffer.getZonas_adicionales();
        for (int i=0; i<zonas_adds.length; ++i) {
            if (zonas_adds[i]) zonas_totales[i].setChecked(true);
        }
        CheckBox[] extras_totales = {ascensor, chimenea, calefaccion, amueblado, aire_acondicionado};
        boolean[] extras = ControladoraOffer.getExtras();
        for (int i=0; i<extras.length; ++i) {
            if (extras[i]) extras_totales[i].setChecked(true);
        }

        // FOTOS
        PreviewFoto0 = findViewById(R.id.previewFoto_EditOffer);
        PreviewFoto1 = findViewById(R.id.previewFoto2_EditOffer);
        PreviewFoto2 = findViewById(R.id.previewFoto3_EditOffer);
        PreviewFoto3 = findViewById(R.id.previewFoto4_EditOffer);
        PreviewFoto4 = findViewById(R.id.previewFoto5_EditOffer);
        PreviewFotos = new ImageView[]{PreviewFoto0, PreviewFoto1, PreviewFoto2, PreviewFoto3, PreviewFoto4};
/*
        //Posem l'estrella de color blanc si l'usuari ja te aquesta oferta com a favorite. En cas contrari la pintem de color negre
        if(ControladoraPresentacio.isFavorite_withID(id))
            afegirFavorite.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        else afegirFavorite.setImageTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
*/
        for (int i = 0; i < 5; ++i) {
            final int finalI = i;
            StorageReference Reference2 = storageRef.child("/products/" + ControladoraPresentacio.getOffer_id()).child("product_" + i);
            System.out.println(Reference2.toString());
            Reference2.getBytes(1000000000).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    cantidad_fotos[0] +=1;
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    //Redondeamos las esquinas de las fotos
                    bmp = ControladoraPresentacio.getRoundedCornerBitmap(bmp,64);
                    PreviewFotos[finalI].setImageBitmap(bmp);
                    PreviewFotos[finalI].setVisibility(View.VISIBLE);
                }
            });
        }
        ControladoraEditOffer.setFotos(PreviewFotos);
        //System.out.println("Cantidad de fotoooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooos" + cantidad_fotos[0]);

        PreviewFoto0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ControladoraEditOffer.add_foto(PreviewFotos[0],0);
                ControladoraEditOffer.setNumero_imagen(0);
                Intent intent = new Intent(VisualizeOffer.this, PreviewFotoShow.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();
            }
        });
        PreviewFoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ControladoraEditOffer.add_foto(PreviewFotos[1],1);
                ControladoraEditOffer.setNumero_imagen(1);
                Intent intent = new Intent(VisualizeOffer.this,  PreviewFotoShow.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();
            }
        });
        PreviewFoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ControladoraEditOffer.add_foto(PreviewFotos[2],2);
                ControladoraEditOffer.setNumero_imagen(2);
                Intent intent = new Intent(VisualizeOffer.this,  PreviewFotoShow.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();
            }
        });
        PreviewFoto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ControladoraEditOffer.add_foto(PreviewFotos[3],3);
                ControladoraEditOffer.setNumero_imagen(3);
                Intent intent = new Intent(VisualizeOffer.this,  PreviewFotoShow.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();
            }
        });
        PreviewFoto4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ControladoraEditOffer.add_foto(PreviewFotos[4],4);
                ControladoraEditOffer.setNumero_imagen(4);
                Intent intent = new Intent(VisualizeOffer.this,  PreviewFotoShow.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();
            }
        });
        new_chat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if (ControladoraPresentacio.getOffer_user().equals(ControladoraPresentacio.getUsername()))
                {
                    String favorite_added_successfully = getString(R.string.error);
                    Toast toast = Toast.makeText(getApplicationContext(), favorite_added_successfully, Toast.LENGTH_SHORT);
                    toast.show();
                }
                else
                {
                    final RequestQueue queue = Volley.newRequestQueue(VisualizeOffer.this);

                    String url = "https://us-central1-test-8ea8f.cloudfunctions.net/get-chats?" + "un=" + ControladoraPresentacio.getUsername();
                    // Request a JSONObject response from the provided URL.
                    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                boolean found = false;
                                for(int i = 0; i < response.length(); ++i) {
                                    JSONObject info_message = response.getJSONObject(i);
                                    String user = info_message.getString("user");
                                    String id_chat = info_message.getString("id");
                                    if (user.equals(ControladoraPresentacio.getOffer_user())) {
                                        found = true;
                                        ControladoraChat.setId_Chat(id_chat);
                                        ControladoraChat.setUsername1(ControladoraPresentacio.getUsername());
                                        ControladoraChat.setUsername2(user);
                                        Intent intent = new Intent(VisualizeOffer.this, VisualizeChat.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                                if (!found) {
                                    String url = "https://us-central1-test-8ea8f.cloudfunctions.net/chat-create?" + "un1=" + ControladoraPresentacio.getUsername() + "&un2=" + ControladoraPresentacio.getOffer_user();
                                    final RequestQueue queue = Volley.newRequestQueue(VisualizeOffer.this);

                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    if (!response.equals("-1")) { //message added successfully
                                                        ControladoraChat.setId_Chat(response);
                                                        ControladoraChat.setUsername1(ControladoraPresentacio.getUsername());
                                                        ControladoraChat.setUsername2(ControladoraPresentacio.getOffer_user());
                                                        Intent intent = new Intent(VisualizeOffer.this, VisualizeChat.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        startActivity(intent);
                                                    }
                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }
                                    });

                                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                    // Add the request to the RequestQueue.
                                    queue.add(stringRequest);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("TAG", "Error Respuesta en JSON: " + error.getMessage());
                        }
                    });
                    queue.add(jsonArrayRequest);
                }
            }
        });


        //Inicilizamos las categorias
        categorias[0] = getString(R.string.add_product_category_technology);
        categorias[1] = getString(R.string.add_product_category_home);
        categorias[2] = getString(R.string.add_product_category_beauty);
        categorias[3] = getString(R.string.add_product_category_sports);
        categorias[4] = getString(R.string.add_product_category_fashion);
        categorias[5] = getString(R.string.add_product_category_leisure);
        categorias[6] = getString(R.string.add_product_category_transport);


        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VisualizeOffer.this, MenuPrincipal.class);
                onNewIntent(intent);
                //startActivity(intent);
                finish();
            }
        });

        Calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VisualizeOffer.this, ScheduleVisitActivity.class);
                startActivity(intent);
                //finish();
            }
        });
/*
        afegirFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Atras.setEnabled(false);
                afegirFavorite.setEnabled(false);
                if(afegirFavorite.getImageTintList() == ColorStateList.valueOf(Color.parseColor("#000000")))
                    RequestAddFavorite(Atras, afegirFavorite, id);
                else
                    RequestDeleteFavorite(id, afegirFavorite, Atras);
            }
        });
        */
    }

    private void RequestAddFavorite(final ImageView Atras, final ImageView afegirFavorite, final String id) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(VisualizeOffer.this);

        String url = "https://us-central1-test-8ea8f.cloudfunctions.net/add-favorite?" +
                "un=" + ControladoraPresentacio.getUsername() + "&" +
                "id=" + ControladoraPresentacio.getOffer_id();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("Favorite Added")) { //favorite added successfully
                            String favorite_added_successfully = getString(R.string.favorite_added_successfully);
                            Toast toast = Toast.makeText(getApplicationContext(), favorite_added_successfully, Toast.LENGTH_SHORT);
                            toast.show();
                            ControladoraPresentacio.addavorite_byId(id);
                            afegirFavorite.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                            Atras.setEnabled(true);
                            afegirFavorite.setEnabled(true);
                        }
                        else if(response.equals("Favorite duplicated")) { //favorite duplicated
                            String favorite_added_successfully = getString(R.string.favorite_added_successfully);
                            Toast toast = Toast.makeText(getApplicationContext(), favorite_added_successfully, Toast.LENGTH_SHORT);
                            toast.show();
                            ControladoraPresentacio.addavorite_byId(id);
                            afegirFavorite.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                            Atras.setEnabled(true);
                            afegirFavorite.setEnabled(true);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String texterror = getString(R.string.error);
                Toast toast = Toast.makeText(VisualizeOffer.this, texterror, Toast.LENGTH_SHORT);
                toast.show();
                //reactivar atras y subir wish
                Atras.setEnabled(true);
                afegirFavorite.setEnabled(true);
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    private void RequestDeleteFavorite(final String id, final ImageView afegirFavorite, final ImageView Atras) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(VisualizeOffer.this);

        String url = "https://us-central1-test-8ea8f.cloudfunctions.net/delete-favorite?" + "un=" + ControladoraPresentacio.getUsername() + "&id=" + id;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("0")) { //Account modified successfully
                            String offer_deleted_successfully = getString(R.string.favorite_removed_successfully);
                            Toast toast = Toast.makeText(getApplicationContext(), offer_deleted_successfully, Toast.LENGTH_SHORT);
                            toast.show();

                            ControladoraPresentacio.deleteFavorite_byId(id);

                            afegirFavorite.setImageTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                            Atras.setEnabled(true);
                            afegirFavorite.setEnabled(true);
                        }
                        else { //response == "1" No s'ha esborrat el desig
                            String texterror = getString(R.string.error);
                            Toast toast = Toast.makeText(VisualizeOffer.this, texterror, Toast.LENGTH_SHORT);
                            toast.show();
                            Atras.setEnabled(true);
                            afegirFavorite.setEnabled(true);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String texterror = getString(R.string.error);
                Toast toast = Toast.makeText(VisualizeOffer.this, texterror, Toast.LENGTH_SHORT);
                toast.show();
                Atras.setEnabled(true);
                afegirFavorite.setEnabled(true);
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //called when image was captured from camera
        if (resultCode == RESULT_OK) {
            //set the image captured to our ImageView
            //int longitud = fotos.length;
            PreviewFotos[cantidad_fotos[0]].setImageURI(image_uri);
            PreviewFotos[cantidad_fotos[0]].setImageURI(image_uri);
            cantidad_fotos[0]++;
            /*
            int longitud = ControladoraAddProduct.getFotos().length;
            int i = 0;
            boolean foto_subida_con_exito = false;
            while ((i < longitud) && (foto_subida_con_exito == false)) {
                if (fotos[i] == null) {
                    PreviewFotos[i].setImageURI(image_uri);
                    PreviewFotos[i].setVisibility(View.VISIBLE);

                    //Actualizamos la controladora
                    ControladoraAddProduct.add_foto(image_uri, i);
                    fotos = ControladoraAddProduct.getFotos();
                    cantidad_fotos = ControladoraAddProduct.getCantidad_fotos();
                    //Salimos del bucle
                    foto_subida_con_exito = true;

                }
                else {
                    //Siguiente hueco de foto
                    ++i;
                }
            }
            */
        }
    }
}
