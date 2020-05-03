// CS 0445 Spring 2020
// This is a partial implementation of the ReallyLongInt class.  You need to
// complete the implementations of the remaining methods.  Also, for this class
// to work, you must complete the implementation of the LinkedListPlus class.
// See additional comments below.

public class ReallyLongInt 	extends LinkedListPlus<Integer> 
							implements Comparable<ReallyLongInt>
{
	private ReallyLongInt()
	{
		super();
	}

	// Data is stored with the LEAST significant digit first in the list.  This is
	// done by adding all digits at the front of the list, which reverses the order
	// of the original string.  Note that because the list is doubly-linked and 
	// circular, we could have just as easily put the most significant digit first.
	// You will find that for some operations you will want to access the number
	// from least significant to most significant, while in others you will want it
	// the other way around.  A doubly-linked list makes this access fairly
	// straightforward in either direction.
	public ReallyLongInt(String s)
	{
		super();
		char c;
		int digit = -1;
		// Iterate through the String, getting each character and converting it into
		// an int.  Then make an Integer and add at the front of the list.  Note that
		// the add() method (from A2LList) does not need to traverse the list since
		// it is adding in position 1.  Note also the the author's linked list
		// uses index 1 for the front of the list.
		for (int i = 0; i < s.length(); i++)
		{
			c = s.charAt(i);
			if (('0' <= c) && (c <= '9'))
			{
				digit = c - '0';
				// Do not add leading 0s
				if (!(digit == 0 && this.getLength() == 0)) 
					this.add(1, new Integer(digit));
			}
			else throw new NumberFormatException("Illegal digit " + c);
		}
		// If number is all 0s, add a single 0 to represent it
		if (digit == 0 && this.getLength() == 0)
			this.add(1, new Integer(digit));
	}

	public ReallyLongInt(ReallyLongInt rightOp)
	{
		super(rightOp);
	}
	
	// Method to put digits of number into a String.  Note that toString()
	// has already been written for LinkedListPlus, but you need to
	// override it to show the numbers in the way they should appear.
	public String toString()
	{
		StringBuilder b = new StringBuilder();
		Node curr = firstNode.prev;
		int i = 0;
		while (i < this.getLength())
		{
			b.append(curr.data.toString());
			curr = curr.prev;
			i++;
		}
		return b.toString();
	}

	// See notes in the Assignment sheet for the methods below.  Be sure to
	// handle the (many) special cases.  Some of these are demonstrated in the
	// RLITest.java program.

	// Return new ReallyLongInt which is sum of current and argument
	public ReallyLongInt add(ReallyLongInt rightOp)
	{
		int carry = 0, n1 = 0, n2 = 0, num = 0, i = 0;
		ReallyLongInt rli = new ReallyLongInt();
		ReallyLongInt smaller, larger;
		if (rightOp.getLength() > getLength()) {
			larger = rightOp;
			smaller = this;
		} else {
			larger = this;
			smaller = rightOp;
		}
		Node next1 = larger.firstNode.getPrevNode(), next2 = smaller.firstNode.getPrevNode();
		for (i = 0; i < larger.getLength(); i++) {
			if (next2 == null) n2 = 0;
			else n2 = next2.getData();
			n1 = next1.getData();
			num = n1 + n2 + carry;
			carry = 0;
			while (num > 9) {
				carry = carry + 1;
				num = num - 10;
			}
			rli.add(new Integer(num));
			next1 = next1.getPrevNode();
			next2 = next2.getPrevNode();		
		}
		if (carry > 0) rli.add(new Integer(carry));
		return rli;
	}
	
	// Return new ReallyLongInt which is difference of current and argument
	public ReallyLongInt subtract(ReallyLongInt rightOp)
	{	
		if (this.compareTo(rightOp) < 0) throw new ArithmeticException();
		int carry = 0, n1 = 0, n2 = 0, num = 0;
		ReallyLongInt rli = new ReallyLongInt();
		ReallyLongInt smaller, larger;
		larger = new ReallyLongInt(this);
		smaller = new ReallyLongInt(rightOp);
		Node next1 = larger.firstNode.getPrevNode(), next2 = smaller.firstNode.getPrevNode();
		for (int i = 0; i <= larger.getLength(); i++) {
			n1 = next1.getData();
			n2 = next2.getData();
			num = n1 - n2 - carry;
			carry = 0;
			if (num < 0) {
				carry = 1;
				num = num + 10;
			}
			rli.add(new Integer(num));
			next1 = next1.getPrevNode();
			next2 = next2.getPrevNode();		
		}
		rli.reverse();
		return rli;
	}

	// Return -1 if current ReallyLongInt is less than rOp
	// Return 0 if current ReallyLongInt is equal to rOp
	// Return 1 if current ReallyLongInt is greater than rOp
	public int compareTo(ReallyLongInt rOp)
	{
		int num = 0;
		if (rOp.getLength() < getLength()) return 1;
		else if (rOp.getLength() > getLength()) return -1;
		else {
			Node next2 = rOp.firstNode.getPrevNode(), next1 = this.firstNode.getPrevNode();
			for (int i = 0; i < getLength(); i++) {
				num = (int)next1.getData() - (int)next2.getData();
				if (num == 0) {
					next1 = next1.getPrevNode();
					next2 = next2.getPrevNode();
				}
				else return num;
			}
		}
		return num;
	}

	// Is current ReallyLongInt equal to rightOp?
	public boolean equals(Object rightOp)
	{
		if (!(rightOp instanceof ReallyLongInt)) return false;
		ReallyLongInt rOp = (ReallyLongInt) rightOp;
		if (!(rOp.getLength() == getLength())) return false;
		else {
			Node next1 = rOp.firstNode, next2 = this.firstNode;
			for (int i = 0; i < getLength(); i++) {
				next1 = next1.getPrevNode();
				next2 = next2.getPrevNode();
				if (!next1.getData().equals(next2.getData())) return false;;
			}
		}
		return true;
	}

	// Mult. current ReallyLongInt by 10^num
	public void multTenToThe(int num)
	{
		Integer q;
		for (int i = 0; i < num; i++) {
			q = new Integer(0);
			add(i, q);
		}
	}

	// Divide current ReallyLongInt by 10^num
	public void divTenToThe(int num)
	{
		leftShift(num-1);
		if (isEmpty()) add(new Integer(0));
	}

}
