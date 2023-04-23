package fr.an.tests.javafxwhiteapp.Visitors;

import fr.an.tests.javafxwhiteapp.Elements.BaseDrawingElements;
import fr.an.tests.javafxwhiteapp.Elements.DrawingElement;

public abstract class DrawingElementVisitor {
    public abstract void caseText(BaseDrawingElements.TextDrawingElement p);
    public abstract void caseLine(BaseDrawingElements.LineDrawingElement p);
    public abstract void caseRect(BaseDrawingElements.RectangleDrawingElement p);
    public abstract void caseCircle(BaseDrawingElements.CircleDrawingElement p);
    public abstract void caseGroup(BaseDrawingElements.GroupDrawingElement p);
    public abstract void caseOther(DrawingElement p);


    public static class TextDrawingElementVisitor extends DrawingElementVisitor {

        public String result;

        public TextDrawingElementVisitor() {
        }

        public TextDrawingElementVisitor(String result) {
            this.result = result;
        }

        @Override
        public void caseText(BaseDrawingElements.TextDrawingElement p) {
            result = "Text(" + p.pos + ",'" + p.text + "')";
        }

        @Override
        public void caseLine(BaseDrawingElements.LineDrawingElement p) {
            result = "Line(" + p.start + ", " + p.end + ")";
        }

        @Override
        public void caseRect(BaseDrawingElements.RectangleDrawingElement p) {
            result = "Rect(" + p.upLeft + ", " + p.downRight + ")";
        }

        @Override
        public void caseCircle(BaseDrawingElements.CircleDrawingElement p) {
            result = "Circle(" + p.center + ", " + p.radius + ")";
        }

        @Override
        public void caseGroup(BaseDrawingElements.GroupDrawingElement p) {
            StringBuilder sb = new StringBuilder();
            sb.append("Group[\n");
            for(DrawingElement child: p.elements) {
                // *** recurse ***
                child.accept(this);
                sb.append(result);
                sb.append("\n");
            }
            sb.append("]");
            result = sb.toString();
        }

        @Override
        public void caseOther(DrawingElement p) {
            result = "not implemented/recognized drawingElement " + p.getClass().getName();
        }
    }
}
