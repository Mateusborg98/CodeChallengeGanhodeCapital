/**
 * 
 */
/**
 * 
 */
module CodeChallengeGanhodeCapital {
	    requires com.fasterxml.jackson.databind;
	    exports br.com.nubank.capitalgain.domain.model;
	    opens br.com.nubank.capitalgain.domain.model to com.fasterxml.jackson.databind;
}