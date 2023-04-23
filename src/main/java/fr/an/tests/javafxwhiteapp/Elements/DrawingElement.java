package fr.an.tests.javafxwhiteapp.Elements;

import fr.an.tests.javafxwhiteapp.Visitors.DrawingElementVisitor;

/**
 * Classe abstraite de base pour les éléments de dessin
 * Héritage de la hiérarchie de classes AST
 * Voir les sous-classes :
 * <ul>
 * <li>TextDrawingElement</li>
 * <li>LineDrawingElement</li>
 * <li>RectangleDrawingElement</li>
 * <li>CircleDrawingElement</li>
 * <li>ImageDrawingElement (adapter design pattern, pour les images : png/jpg/gif/...)</li>
 * <li>GroupDrawingElement (composite design pattern)</li>
 * <li>Autres...</li>
 * </ul>
 */
public abstract class DrawingElement {

    /**
     * Visitor design pattern
     * implement in sub-class, to call <code> visitor.caseXX(this); </code>
     */
    public abstract void accept(DrawingElementVisitor visitor);
}

