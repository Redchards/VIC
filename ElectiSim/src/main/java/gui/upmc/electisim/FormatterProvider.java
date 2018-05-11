package gui.upmc.electisim;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.function.UnaryOperator;

import javafx.scene.control.TextFormatter;

public class FormatterProvider {
	private static final NumberFormat numberFormat = NumberFormat.getInstance();
	
	private static final UnaryOperator<TextFormatter.Change> formatOperator = c ->
	{
	    if ( c.getControlNewText().isEmpty() )
	    {
	        return c;
	    }

	    ParsePosition parsePosition = new ParsePosition( 0 );
	    Object object = numberFormat.parse( c.getControlNewText(), parsePosition );

	    if ( object == null || parsePosition.getIndex() < c.getControlNewText().length() )
	    {
	        return null;
	    }
	    else
	    {
	        return c;
	    }
	};
	
	static final TextFormatter getNumberFormatter() {
		return new TextFormatter<>(formatOperator);
	}
}
