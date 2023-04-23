package fr.an.tests.javafxwhiteapp.Elements;

import fr.an.tests.javafxwhiteapp.Visitors.DrawingElementVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseDrawingElements {

    public static class TextDrawingElement extends DrawingElement {
        public String text;
        public DrawingPt pos;
        public Map<String,Object> properties; // font, size, color,

        public TextDrawingElement() {
            super();
        }

        public TextDrawingElement(String text,DrawingPt pos) {
            super();
            this.text=text;
            this.pos = pos;
        }

        @Override
        public void accept(DrawingElementVisitor visitor) {
            visitor.caseText(this);
        }
    }

    public static class LineDrawingElement extends DrawingElement {
        public DrawingPt start;
        public DrawingPt end;
        public Map<String,Object> properties; // width, stroke, color, ..

        public LineDrawingElement() {
            super();
        }

        public LineDrawingElement(DrawingPt start, DrawingPt end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public void accept(DrawingElementVisitor visitor) {
            visitor.caseLine(this);
        }

    }
    public static class RectangleDrawingElement extends DrawingElement {
        public DrawingPt upLeft;
        public DrawingPt downRight;
        public Map<String,Object> properties; // width, stroke, color, ..

        public RectangleDrawingElement() {
            super();
        }

        public RectangleDrawingElement(DrawingPt upLeft, DrawingPt downRight) {
            this.upLeft = upLeft;
            this.downRight = downRight;
        }

        @Override
        public void accept(DrawingElementVisitor visitor) {
            visitor.caseRect(this);
        }
    }
    public static class CircleDrawingElement extends DrawingElement {
        public DrawingPt center;
        public double radius;
        public Map<String,Object> properties; // width, stroke, color, ..

        public CircleDrawingElement() {
            super();
        }

        public CircleDrawingElement(DrawingPt center, double radius) {
            this.center = center;
            this.radius = radius;
        }

        @Override
        public void accept(DrawingElementVisitor visitor) {
            visitor.caseCircle(this);
        }
    }

    /**
     * Composite design pattern
     */
    public static class GroupDrawingElement extends DrawingElement {
        public List<DrawingElement> elements = new ArrayList<>();

        public void add(DrawingElement drawingElement) {
            this.elements.add(drawingElement);
        }

        @Override
        public void accept(DrawingElementVisitor visitor) {
            visitor.caseGroup(this);
        }
    }

    /**
     * Adapter design pattern, for javafx.scene.image.Image
     */
    public static class ImageDrawingElement extends DrawingElement {
        public javafx.scene.image.Image image; // => url, mimeType, data

        @Override
        public void accept(DrawingElementVisitor visitor) {
            visitor.caseOther(this);
        }
    }

    /**
     * Proxy design pattern
     * example: including part from another document
     */
    public static class ProxyDrawingElement extends DrawingElement {
        public DrawingElement underlying;

        @Override
        public void accept(DrawingElementVisitor visitor) {
            visitor.caseOther(this);
        }
    }
    /**
     * Decorator design pattern
     * For geometrical affine transformation
     */
    public static class AffineTransformedDrawingElement extends DrawingElement {
        public DrawingElement underlying;
        public DrawingPt translate;
        public double rotateAngle;
        public double scale;

        @Override
        public void accept(DrawingElementVisitor visitor) {
            visitor.caseOther(this);
        }
    }

    public GroupDrawingElement createSimpleDrawing() {
        TextDrawingElement text = new TextDrawingElement();
        text.text = "Hello";
        text.pos = new DrawingPt(100, 100);

        LineDrawingElement line = new LineDrawingElement();
        line.start = new DrawingPt(100, 130);
        line.end = new DrawingPt(200, 230);

        RectangleDrawingElement rectangle = new RectangleDrawingElement();
        rectangle.upLeft = new DrawingPt(100, 300);
        rectangle.downRight = new DrawingPt(200, 350);

        CircleDrawingElement circle = new CircleDrawingElement();
        circle.center = new DrawingPt(150, 400);
        circle.radius = 45;

        GroupDrawingElement res = new GroupDrawingElement();
        res.elements.add(text);
        res.elements.add(line);
        res.elements.add(rectangle);
        res.elements.add(circle);

        return res;
    }
}