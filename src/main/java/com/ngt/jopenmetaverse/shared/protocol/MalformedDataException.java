package com.ngt.jopenmetaverse.shared.protocol;


    /// <summary>
    /// Thrown when a packet could not be successfully deserialized
    /// </summary>
    public class MalformedDataException extends Exception
    {
        /// <summary>
        /// Default constructor
        /// </summary>
        public MalformedDataException() 
        { }

        /// <summary>
        /// Constructor that takes an additional error message
        /// </summary>
        /// <param name="Message">An error message to attach to this exception</param>
        public MalformedDataException(String message)
        {
        	super(message);
        }
    }