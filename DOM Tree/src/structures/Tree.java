package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {
	
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	
	/**
	 * Builds the DOM tree from input HTML file, through scanner passed
	 * in to the constructor and stored in the sc field of this object. 
	 * 
	 * The root of the tree that is built is referenced by the root field of this object.
	 */
	public void build() {
		if(sc == null) return;
		//set root to be the TagNode passed by builder method
		root = build2();
//		print();
	}
	
	/**
	 * Method used to recursively set nodes with siblings and children
	 * 
	 * @return
	 */
	private TagNode build2() {
		if(sc.hasNextLine() == true) {
			//store word from tagnode
			String current = sc.nextLine();
			//if this is </>
			if(current.length() > 1 && current.charAt(1) == '/') {
				//don't include
				return null;
			} else if(current.charAt(0) == '<') {
				//delete <>
				current = current.substring(1, current.length() - 1);
				TagNode next = new TagNode(current, null, null);
				//set children nodes until </> reached
				next.firstChild = build2();
				//move to sibling nodes
				next.sibling = build2();
				return next;
			} else {
				TagNode next = new TagNode(current, null, null);
				next.sibling = build2();
				//System.out.println(next);
				return next;
			}
		}
		else return null;
	}
	
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {
		if(root == null || oldTag == null || newTag == null) return;
		
		replaceTag2(root, oldTag, newTag);
	}
	
	/** Loop through tree recursively and replace depth first
	 * 
	 * @param root
	 * @param oldTag
	 * @param newTag
	 */
	private void replaceTag2(TagNode root, String oldTag, String newTag) {
		if(root == null || oldTag == null || newTag == null) return;
		replaceTag2(root.firstChild, oldTag, newTag);
		
		if(root.tag.equals(oldTag)) {
			root.tag = newTag;
		}
		
		replaceTag2(root.sibling, oldTag, newTag);
//		print();
	}
	
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {
		if(root == null || row <= 0) return;
		boldRow2(root, root.firstChild, row, 0);
		
//		TagNode prev = root;
//		TagNode prevPointer = prev;
//		TagNode current = root.firstChild;
//		TagNode currentPointer = current;
//		int count = 0;
//traverse entire tree from root to leaf, when tr found, add b node
//		while(current != null) {
//			while(currentPointer != null) {
//				if(currentPointer.tag.equals("tr")) {
//					count++;
//				} else if(count == row && currentPointer.firstChild == null) {
//					prevPointer.firstChild = new TagNode("b", currentPointer, null);
//				}
//				prevPointer = traverseChild(prevPointer);
//				currentPointer = traverseChild(currentPointer);
//			}
//			if(prevPointer.sibling != null) {
//			} else {
//			}
//			if(current.sibling != null)
//				current = traverseSibling(current);
//			else {
//				prev = traverseChild(prevPointer);
//				current = traverseChild(current);
//			}
//			prevPointer = current;
//			currentPointer = traverseSibling(prevPointer);
//		}
//		print();

	}
	
	private void boldRow2(TagNode prev, TagNode current, int row, int count) {
		if(current == null) {
			return;
		} else if(current.tag.equals("tr")) {
			count++;
		} else if(count == row && current.firstChild == null) {
			prev.firstChild = new TagNode("b", current, null);
		}
		//iterate row
		boldRow2(current, current.firstChild, row, count);
		//iterate column
		boldRow2(current, current.sibling, row, count);
	}

	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag) {
		if(root == null) return;
		removeHelper(tag, root, root.firstChild);
//		print();
//		
//		TagNode prev = root;
//		TagNode current = root.firstChild;
//		
//		if(current.tag.equals(tag)) {
//			//if tag is ol or ul, remove folowwing li
//			if(tag.equals("ol") || tag.equals("ul")) {
//				//convert all li tags to p tags
//				TagNode temp = current.firstChild;
//				while(temp.tag.equals("li")) {
//					temp.tag = "p";
//					temp = temp.sibling;
//				}
//			}
//			//current's tag = p, em, b, ol, or ul
//			prev.firstChild = current.firstChild;
//		}
//		//removeTag(tag);
//		print();
	}
	
	private void removeHelper(String tag, TagNode prev, TagNode current) {
		if(prev == null || current == null) return;
		//if html tag is to be removed
		if(prev.tag.equals(tag)) {
			root = current;
			return;
		}
		
		//current node matches tag
		if(current.tag.equals(tag)) {
			//keep track of sibling reassignment
			TagNode adjustSiblingNode = null;
			if(current != null) {		//will always be != null since check is in beginning of method
				adjustSiblingNode = current;
			}
			//call private method
			if(current == prev.firstChild) {
				//set prev.firstchild to current.firstchild
				//set prev.firstchild to current.sibling IF current.firstchild is null to avoid NullPointerException
				remove2(prev, current);
			}
			else {
				//current is not the first child, need to find the node which has sibling node == current
				TagNode finder = prev.firstChild;
				while(!finder.sibling.equals(current)) {
					finder = finder.sibling;
				}
				//finder is now the node above current. finder.sibling is current
				//delete current by setting finder.sibling to current.firstchild
				remove2(finder, current);
			}
			//set the sibling of the deleted node (current) to the new current node
			if(adjustSiblingNode.sibling != null) {
				TagNode temp = current.firstChild;
				if(temp != null) {
					while(temp.sibling != null)
					temp = temp.sibling;
					temp.sibling = adjustSiblingNode.sibling;
				}
				//print();
			}
			removeHelper(tag, prev, prev.firstChild);
			
//			if(adjustSiblingNode != null) {
//				//setting prev.fc.sibling to adjustSiblingNode
//				TagNode newlyAdded = prev.firstChild;
//				while(newlyAdded.sibling != null) {
//					newlyAdded = newlyAdded.sibling;
//				}
//				newlyAdded.sibling = adjustSiblingNode;
//			}
			
			//removeHelper(tag, prev, current.sibling);
//			if(prev.sibling == current) {
//				if(current.sibling != null) {
//					current.firstChild.sibling = current.sibling;
//				}
//				prev.sibling = current.firstChild;
//			}
//			prev.sibling = current.firstChild;
		} else if(current.tag.equals("li") && (tag == "ol" || tag == "ul")) {
			current.tag = "p";
		} else {
			removeHelper(tag, current, current.firstChild);
			removeHelper(tag, prev, current.sibling);
		}
	}
	
	private void remove2(TagNode prev, TagNode current) {
		if(prev == null || current == null) return;
		//if node is found to be a firstchild
		if(current.equals(prev.firstChild)) {
			if(current.firstChild != null)
				prev.firstChild = current.firstChild;
			else	//there is no child for current, set prev.firstchild as current.sibling
				prev.firstChild = current.sibling;
		}
		else {
			//current = prev.sibling
			prev.sibling = current.firstChild;
//	
//			//current is not first child of prev, loop through siblings to find node
//			TagNode temp = prev.firstChild;
//			while(!temp.equals(current)) {
//				temp = temp.sibling;
//			}
//			//mark node to delete
//			TagNode delete = temp;
//			delete = delete.firstChild;
//			current = delete;
//			TagNode temp2 = temp;
//			temp2 = temp2.sibling;
//			
//			//set temp to where current is
//			//temp = temp.sibling;
//			//current = current.firstChild;
//			//TagNode sibling = current;
//			while(current.sibling != null) {
//				current = current.sibling;
//			}
//			current.sibling = temp;
		}
	}
	
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	public void addTag(String word, String tag) {
	}
/*	
	private static TagNode traverseChild(TagNode root) {
		TagNode current = root;
		current = current.firstChild;
		return current;
	}
	
	private static TagNode traverseSibling(TagNode root) {
		TagNode current = root;
		
		if(current == null) return current;
		
		if(current.sibling != null) {
			current = current.sibling;
			return current;
		}
		
		return current;
	}
*/
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
	/**
	 * Prints the DOM tree. 
	 *
	 */
	public void print() {
		print(root, 1);
	}
	
	private void print(TagNode root, int level) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			for (int i=0; i < level-1; i++) {
				System.out.print("      ");
			};
			if (root != this.root) {
				System.out.print("|----");
			} else {
				System.out.print("     ");
			}
			System.out.println(ptr.tag);
			if (ptr.firstChild != null) {
				print(ptr.firstChild, level+1);
			}
		}
	}
	
}
