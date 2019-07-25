package apps.nocturnuslabs.digitrecognizer;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import apps.nocturnuslabs.digitrecognizer.models.Classification;
import apps.nocturnuslabs.digitrecognizer.models.Classifier;
import apps.nocturnuslabs.digitrecognizer.models.TensorFlowClassifier;
import apps.nocturnuslabs.digitrecognizer.views.DrawModel;
import apps.nocturnuslabs.digitrecognizer.views.DrawView;

public class MainActivity extends Activity implements View.OnTouchListener, View.OnClickListener {

    private static final int PIXEL_WIDTH = 28;
    private static final int PIXEL_HEIGHT = 28;

    private Button clearBtn, detectBtn;
    private TextView resultText;
    private List<Classifier> mClassifiers = new ArrayList<>();

    private DrawModel drawModel;
    private DrawView drawView;
    private PointF mTmpPoint = new PointF();

    private float mLastX;
    private float mLastY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawView = (DrawView) findViewById(R.id.main_draw);
        drawModel = new DrawModel(PIXEL_WIDTH, PIXEL_HEIGHT);

        drawView.setModel(drawModel);
        drawView.setOnTouchListener(this);

        clearBtn = (Button) findViewById(R.id.main_clearbtn);
        clearBtn.setOnClickListener(this);

        detectBtn = (Button) findViewById(R.id.main_detectbtn);
        detectBtn.setOnClickListener(this);

        resultText = (TextView) findViewById(R.id.main_resultbox);

        loadModel();
    }

    private void loadModel() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                try{

                    mClassifiers.add(
                            TensorFlowClassifier.create(getAssets(),"Keras",
                                    "opt_mnist_convnet-keras.pb","labels.txt",PIXEL_WIDTH,
                                    "conv2d_1_input","dense_2/Softmax",false)
                    );

                }catch(final Exception e){
                    throw new RuntimeException("Error initializing classifier", e);
                }
            }
        }).start();

    }

    @Override
    protected void onResume() {
        drawView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        drawView.onPause();
        super.onPause();
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.main_clearbtn){
            drawModel.clear();
            drawView.reset();
            drawView.invalidate();
            resultText.setText("");

        }else if(v.getId() == R.id.main_detectbtn){

            float pixels[] = drawView.getPixelData();
            String text = "";
            for (Classifier classifier : mClassifiers) {
                //perform classification on the image
                final Classification res = classifier.recognize(pixels);
                //if it can't classify, output a question mark
                if (res.getLabel() == null) {
                    text += classifier.name() + ": ?\n";
                } else {
                    //else output its name
                    text += String.format("%s: %s, %f\n", classifier.name(), res.getLabel(),
                            res.getConf());
                }
            }

            resultText.setText(text);

        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int action = event.getAction() & MotionEvent.ACTION_MASK;
        if (action == MotionEvent.ACTION_DOWN) {
            processTouchDown(event);
            return true;

        }else if (action == MotionEvent.ACTION_MOVE) {
            processTouchMove(event);
            return true;

        }else if (action == MotionEvent.ACTION_UP) {
            v.performClick();
            processTouchUp();
            return true;
        }

        return false;
    }

    private void processTouchDown(MotionEvent event) {
        mLastX = event.getX();
        mLastY = event.getY();
        drawView.calcPos(mLastX, mLastY, mTmpPoint);

        float lastConvX = mTmpPoint.x;
        float lastConvY = mTmpPoint.y;
        drawModel.startLine(lastConvX, lastConvY);
    }

    private void processTouchMove(MotionEvent event){
        float x = event.getX();
        float y = event.getY();

        drawView.calcPos(x, y, mTmpPoint);
        float newConvX = mTmpPoint.x;
        float newConvY = mTmpPoint.y;
        drawModel.addLineElem(newConvX, newConvY);

        mLastX = x;
        mLastY = y;
        drawView.invalidate();
    }

    private void processTouchUp() {
        drawModel.endLine();
    }
}
