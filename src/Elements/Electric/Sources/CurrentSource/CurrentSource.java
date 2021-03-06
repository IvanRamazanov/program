/*
 * The MIT License
 *
 * Copyright 2018 Ivan.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package Elements.Electric.Sources.CurrentSource;

import ElementBase.ElectricPin;
import ElementBase.Element;
import ElementBase.SchemeElement;
import Elements.Environment.Subsystem.Subsystem;

/**
 *
 * @author Ivan
 */
public class CurrentSource extends SchemeElement {
    public CurrentSource(Subsystem sys){
        super(sys);
        addElectricCont(new ElectricPin(this, 12, 5));
        addElectricCont(new ElectricPin(this, 12, 60));
    }

    public CurrentSource(boolean catalog){
        super(catalog);
    }

    @Override
    public String[] getStringFunction() {
        String A=this.parameters.get(0).toString();
        String[] str={
                "i.2="+A,
                "i.1+i.2=0"
        };
        return str;
    }

    @Override
    protected void setParams(){
        this.parameters.add(new Element.ScalarParameter("Amplitude", 15.0));
        setName("DC current source");
    }

    @Override
    protected String getDescription(){
        return "This block represents a current source.";
    }
}

