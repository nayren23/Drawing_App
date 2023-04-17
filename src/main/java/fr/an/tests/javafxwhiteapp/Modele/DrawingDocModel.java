package fr.an.tests.javafxwhiteapp.Modele;

import java.util.ArrayList;
import java.util.List;

public class DrawingDocModel {

    protected List<DrawingModelListener> listeners = new ArrayList<>();

    public String documentName;
    // to be replaced next
    // by Drawing AST classes
    public String content;

    public void addListener(DrawingModelListener listener) {
        listeners.add(listener);
    }

    public void removeListener(DrawingModelListener listener) {
        listeners.remove(listener);
    }

    private void fireModelChange() {
        for (DrawingModelListener listener : listeners) {
            listener.onModelChange();
        }
    }


    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


// more later: Publisher design-pattern
}
