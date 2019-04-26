package com.levez.expandablecard;

/**
 * ExpandableViewException - This class is used to handle exceptions
 * of the ExpandableView class.
 *
 * @author  Felipe Levez
 * @version 0.1.0
 * @see java.lang.RuntimeException
 * @see ExpandableView
 */
class ExpandableViewException extends RuntimeException{

    ExpandableViewException(String message) {
        super(message);
    }


    /**
     * LayoutChildNotFound - This class is used to handle exceptions
     * of the ExpandableView class when the child is not found
     *
     * @author  Felipe Levez
     * @version 0.1.0
     * @see java.lang.RuntimeException
     * @see ExpandableViewException
     * @see ExpandableView
     */
    static class LayoutChildNotFound extends ExpandableViewException {
        LayoutChildNotFound() {
            super("Child layout not found.");
        }
    }

    /**
     * TitleNotFound - This class is used to handle exceptions
     * of the ExpandableView class when a title is not found.
     *
     * @author  Felipe Levez
     * @version 0.1.0
     * @see java.lang.RuntimeException
     * @see ExpandableViewException
     * @see ExpandableView
     */
    static class TitleNotFound extends ExpandableViewException {
        TitleNotFound() {
            super("Title not found.");
        }
    }
}
