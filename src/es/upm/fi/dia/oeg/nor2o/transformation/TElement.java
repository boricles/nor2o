/*
 * Copyright (C) 2009 Ontology Engineering Group, Departamento de Inteligencia Artificial
 * 					  Facultad de Inform�tica, Universidad Polit�cnica de Madrid, Spain
 * 					  boricles
 *	
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package es.upm.fi.dia.oeg.nor2o.transformation;

/**
 * @author boricles
 *
 */
public class TElement {

	protected String from;
	protected String to;
	
	protected String inverse;
	
	protected String iF;
	
	public TElement() {
		super();
		
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getInverse() {
		return inverse;
	}

	public void setInverse(String inverse) {
		this.inverse = inverse;
	}

	public String getIf() {
		return iF;
	}

	public void setIf(String iF) {
		this.iF = iF;
	}	

}
