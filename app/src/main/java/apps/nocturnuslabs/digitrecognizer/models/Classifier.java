package apps.nocturnuslabs.digitrecognizer.models;

public interface Classifier {
    String name();

    Classification recognize(final float[] pixels);
}
