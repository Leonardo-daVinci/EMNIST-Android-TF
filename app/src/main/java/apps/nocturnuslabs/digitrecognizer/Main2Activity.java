package apps.nocturnuslabs.digitrecognizer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import apps.nocturnuslabs.digitrecognizer.views.DrawingView;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    private Button drawBtn, startBtn, erase, save;
    private DrawingView drawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        drawingView = findViewById(R.id.draw_drawing);

        drawBtn = findViewById(R.id.draw_drawbtn);
        drawBtn.setOnClickListener(this);

        startBtn = findViewById(R.id.draw_newbtn);
        startBtn.setOnClickListener(this);

        erase = findViewById(R.id.draw_erasebtn);
        erase.setOnClickListener(this);

        save = findViewById(R.id.draw_savebtn);
        save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.draw_drawbtn) {
            drawingView.setupDrawing();
        }
        if (v.getId() == R.id.draw_erasebtn) {
            drawingView.setErase(true);
            drawingView.setBrushSize(drawingView.getBrushSize());
        }
        if (v.getId() == R.id.draw_newbtn) {
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setTitle("New Drawing");
            newDialog.setMessage("Start new drawing?");
            newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    drawingView.startNew();
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        }

    }


}

