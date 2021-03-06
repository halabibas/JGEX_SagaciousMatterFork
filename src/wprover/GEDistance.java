package wprover;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.w3c.dom.Element;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2005-1-19
 * Time: 10:16:32
 * To change this template use File | Settings | File Templates.
 */
public class GEDistance extends GraphicEntity {

    GEPoint pstart;
    GEPoint pend;
    double len = 18;

    public GEDistance() {
        super(GraphicEntity.DISTANCE);
    }

    public GEDistance(GEPoint p1, GEPoint p2) {
        super(GraphicEntity.DISTANCE);
        m_color = 6;
        pstart = p1;
        pend = p2;
        m_name = "|" + p1.getname() + p2.getname() + "|";
        if (pstart == null || pend == null)
            UtilityMiscellaneous.print("Neither endpoint of the Distance can be null");
    }

    public GEDistance(DrawPanel dp, Element thisElement, Map<Integer, GraphicEntity> mapGE) {
    	super(dp, thisElement);
    	
		int index = DrawPanelFrame.safeParseInt(thisElement.getAttribute("start_point"), 0);
		GraphicEntity ge = mapGE.get(index);
		if (ge instanceof GEPoint)
			pstart = (GEPoint)ge;
		else
			bIsValidEntity = false;
		
		index = DrawPanelFrame.safeParseInt(thisElement.getAttribute("end_point"), 0);
		ge = mapGE.get(index);
		if (ge instanceof GEPoint)
			pend = (GEPoint)ge;
		else
			bIsValidEntity = false;
    }
    
    
    /* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		long temp;
		temp = Double.doubleToLongBits(len);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((pend == null) ? 0 : pend.hashCode());
		result = prime * result + ((pstart == null) ? 0 : pstart.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof GEDistance))
			return false;
		GEDistance other = (GEDistance) obj;
		if (Double.doubleToLongBits(len) != Double.doubleToLongBits(other.len))
			return false;
		if (pend == null) {
			if (other.pend != null)
				return false;
		} else if (!pend.equals(other.pend))
			return false;
		if (pstart == null) {
			if (other.pstart != null)
				return false;
		} else if (!pstart.equals(other.pstart))
			return false;
		return true;
	}

	public final String TypeString() {
        String st = Language.getLs(124);
        if (st == null)
            st = "distance";
        if (m_name == null)
        	return st;
        return st + " " + m_name;
    }

    public final String getDescription() {
        String st = Language.getLs(124);
        if (st == null)
            st = "distance";

        return st + " " + pstart.m_name + " " + pend.m_name;
    }

    public void drag(double xx, double yy) {
        double x1, y1, x2, y2, x3, y3;
        x1 = pstart.getx();
        y1 = pstart.gety();
        x2 = pend.getx();
        y2 = pend.gety();
        x3 = xx;
        y3 = yy;

        double x = x2 - x1;
        double y = y2 - y1;

        double dis = Math.sqrt(x * x + y * y);
        if (dis != 0) {
        	double dx = x / dis;
	        double dy = y / dis;
	        double m = x3 - x1;
	        double n = y3 - y1;
	        len = m * dy - n * dx;
        }
    }

    public boolean isLocatedNear(double xx, double yy) {
        double x1, y1, x2, y2, x3, y3;
        x1 = pstart.getx();
        y1 = pstart.gety();
        x2 = pend.getx();
        y2 = pend.gety();
        x3 = xx;
        y3 = yy;


        double x = x2 - x1;
        double y = y2 - y1;

        double dis = Math.sqrt(x * x + y * y);
        if (dis != 0) {
	        double dx = x / dis;
	        double dy = y / dis;
	        double m = x3 - x1;
	        double n = y3 - y1;
	        double length = m * dy - n * dx;
	        if (Math.abs(length - len) < UtilityMiscellaneous.PIXEPS)
	            return true;
        }
        return false;
    }

//    public void draw(Graphics2D g2) {
//        draw(g2, false);
//    }

    public void draw(Graphics2D g2, boolean isSelected) {
        if (!isdraw()) return;

        double x1, y1, x2, y2;
        x1 = pstart.getx();
        y1 = pstart.gety();
        x2 = pend.getx();
        y2 = pend.gety();

        double x = x2 - x1;
        double y = y2 - y1;
        if (x == 0 && y == 0) return;

        double dis = Math.sqrt(x * x + y * y);
        double dx = x / dis;
        double dy = y / dis;
        double xx1 = x1 + dy * len;
        double yy1 = y1 - dx * len;
        double xx2 = x2 + dy * len;
        double yy2 = y2 - dx * len;
        double sin = Math.sin(Math.PI / 6);
        double cos = Math.cos(Math.PI / 6);
        double ddx = dx * 12;
        double ddy = dy * 12;
        double px1 = xx1 + ddx * cos - ddy * sin;
        double py1 = yy1 + ddx * sin + ddy * cos;
        double px2 = xx1 + ddx * cos + ddy * sin;
        double py2 = yy1 - ddx * sin + ddy * cos;


        ddx = -ddx;
        ddy = -ddy;
        double qx1 = xx2 + ddx * cos - ddy * sin;
        double qy1 = yy2 + ddx * sin + ddy * cos;
        double qx2 = xx2 + ddx * cos + ddy * sin;
        double qy2 = yy2 - ddx * sin + ddy * cos;


        if (!isSelected) {
            //g2.setStroke(new BasicStroke(1));
            prepareToBeDrawnAsUnselected(g2);
            //g2.setColor(Color.cyan);
        } else {
            g2.setStroke(new BasicStroke(3));
            g2.setColor(Color.pink);
        }
        g2.drawLine((int) x1, (int) y1, (int) (xx1 + 8 * dy), (int) (yy1 - 8 * dx));
        g2.drawLine((int) x2, (int) y2, (int) (xx2 + 8 * dy), (int) (yy2 - 8 * dx));
        g2.drawLine((int) xx1, (int) yy1, (int) px1, (int) py1);
        g2.drawLine((int) xx1, (int) yy1, (int) px2, (int) py2);
        g2.drawLine((int) xx2, (int) yy2, (int) qx1, (int) qy1);
        g2.drawLine((int) xx2, (int) yy2, (int) qx2, (int) qy2);

        g2.drawLine((int) xx1, (int) yy1, (int) xx2, (int) yy2);
        g2.setColor(Color.black);
        g2.drawString("" + pstart.m_name + pend.m_name + " = " + ((float) ((int) (dis * 100))) / 100, (int) ((xx1 + xx2) / 2), (int) ((yy1 + yy2) / 2));
    }

    public void SavePS(FileOutputStream fp, int stype) throws IOException {
        if (!isdraw()) return;
        double x1, y1, x2, y2;
        x1 = pstart.getx();
        y1 = pstart.gety();
        x2 = pend.getx();
        y2 = pend.gety();

        double x = x2 - x1;
        double y = y2 - y1;

        double dis = Math.sqrt(x * x + y * y);
        if (dis == 0)
        	return;
        double dx = x / dis;
        double dy = y / dis;
        double xx1 = x1 + dy * len;
        double yy1 = y1 - dx * len;
        double xx2 = x2 + dy * len;
        double yy2 = y2 - dx * len;
        double sin = Math.sin(Math.PI / 6);
        double cos = Math.cos(Math.PI / 6);
        double ddx = dx * 12;
        double ddy = dy * 12;
        double px1 = xx1 + ddx * cos - ddy * sin;
        double py1 = yy1 + ddx * sin + ddy * cos;
        double px2 = xx1 + ddx * cos + ddy * sin;
        double py2 = yy1 - ddx * sin + ddy * cos;


        ddx = -ddx;
        ddy = -ddy;
        double qx1 = xx2 + ddx * cos - ddy * sin;
        double qy1 = yy2 + ddx * sin + ddy * cos;
        double qx2 = xx2 + ddx * cos + ddy * sin;
        double qy2 = yy2 - ddx * sin + ddy * cos;

        String s = "";
        s += GraphicEntity.getPSLineString((int) x1, -(int) y1, (int) (xx1 + 8 * dy), -(int) (yy1 - 8 * dx));
        s += GraphicEntity.getPSLineString((int) x2, -(int) y2, (int) (xx2 + 8 * dy), -(int) (yy2 - 8 * dx));
        s += GraphicEntity.getPSLineString((int) xx1, -(int) yy1, (int) px1, -(int) py1);
        s += "\n";

        s += GraphicEntity.getPSLineString((int) xx1, -(int) yy1, (int) px2, -(int) py2);
        s += GraphicEntity.getPSLineString((int) xx2, -(int) yy2, (int) qx1, -(int) qy1);
        s += GraphicEntity.getPSLineString((int) xx2, -(int) yy2, (int) qx2, -(int) qy2);
        s += "\n";
        s += GraphicEntity.getPSLineString((int) xx1, -(int) yy1, (int) xx2, -(int) yy2);
        fp.write(s.getBytes());
        super.saveSuper(fp);

        String sv = pstart.m_name + pend.m_name + " = " + ((float) ((int) (dis * 100))) / 100;
        s = "black";
        s += " mf " + (int) ((xx1 + xx2) / 2) + " " + (-(int) ((yy1 + yy2) / 2) - 15) + " moveto (" + sv + ") " + "show\n";
        fp.write(s.getBytes());
    }

    @Override
    public Element saveIntoXMLDocument(Element rootElement, String sTypeName) {
    	assert(rootElement != null);
    	if (rootElement != null) {
    		Element elementThis = super.saveIntoXMLDocument(rootElement, "distance");

    		elementThis.setAttribute("start_point", String.valueOf(pstart.m_id));
    		elementThis.setAttribute("end_point", String.valueOf(pend.m_id));
    		
    		return elementThis;
		}
    	return null;
	}
	
//	public void Save(DataOutputStream out) throws IOException {
//        super.Save(out);
//        out.writeInt(pstart.m_id);
//        out.writeInt(pend.m_id);
//        out.writeDouble(len);
//    }
//
//    public void Load(DataInputStream in, drawProcess dp) throws IOException {
//        super.Load(in, dp);
//        if (CMisc.version_load_now < 0.01) {
//            m_id = in.readInt();
//            pstart = dp.getPointById(in.readInt());
//            pend = dp.getPointById(in.readInt());
//        } else {
//            pstart = dp.getPointById(in.readInt());
//            pend = dp.getPointById(in.readInt());
//            if (CMisc.version_load_now >= 0.020)
//                len = in.readDouble();
//        }
//    }
}
