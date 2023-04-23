package fr.an.tests.javafxwhiteapp.Modele;

import fr.an.tests.javafxwhiteapp.Elements.DrawingElement;

import java.util.ArrayList;
import java.util.List;

public class DrawingDocModel {

    protected List<DrawingModelListener> listeners = new ArrayList<>();
    protected DrawingElement content; // was String before step8

    public void addListener(DrawingModelListener listener) {
        listeners.add(listener);
    }

    private void fireModelChange() {
        for (DrawingModelListener listener : listeners) {
            listener.onModelChange();
        }
    }

    public DrawingElement getContent() {
        return content;
    }

    public void setContent(DrawingElement content) {
        this.content = content;
        fireModelChange(); // Appeler la méthode pour signaler un changement de modèle
    }

// more later: Publisher design-pattern
}