package com.serviceorder.cache.keygen;

import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Created by Rakesh Komulwad on 6/4/2014.
 */
public abstract class AbstractKeyGenerator implements KeyGenerator {
    protected static final String SEPERATOR = ".";

  /**
   *
   * @param evaluationDate
   * @return if not null returns Date in yyyyMMdd format else empty string
   */
    protected String convertDateToString(XMLGregorianCalendar evaluationDate) {
      if(evaluationDate == null)
        return "";

      return evaluationDate != null ? (evaluationDate.getYear()+""+evaluationDate.getMonth()+""+evaluationDate.getDay()) : "";
    }
}
