package jp.co.worksap.recruiting;

class TreeNode<T> {
	public boolean black;
	public TreeNode<T> p;// parent reference
	public TreeNode<T> lchild;// left child reference
	public TreeNode<T> rchild;// right child reference
	public T key;

	public TreeNode() {
		black = false;
		p = this;
		lchild = this;
		rchild = this;
	}

	public TreeNode(T key) throws Exception {
		this();
		if (!(key instanceof Comparable)) {
			throw new Exception("key cann't be compared!");
		}
		this.key = key;
	}

	/**
	 * change the color of this node
	 * 
	 * @return the color after changing
	 */
	public char switchColor() {
		black = !black;
		return black ? 'b' : 'r';
	}
	
	public TreeNode<T> searchTraverse(T t){
		if(this.key==t){
			return this;
		}
		else{
			TreeNode<T> leftRes=null;
			TreeNode<T> rightRes=null;
			if(lchild.key!=null){
				leftRes=lchild.searchTraverse(t);
			}
			else{
				return null;
			}
			if(rchild.key!=null){
				rightRes=rchild.searchTraverse(t);
			}
			else{
				return null;
			}
			
			if(leftRes!=null){
				return leftRes;
			}
			else if(rightRes!=null){
				return rightRes;
			}
			else{
				return null;
			}
		}
	}
}

public class RBTree<T> {
	public final TreeNode<T> nil = new TreeNode<T>();
	private TreeNode<T> root;
	private boolean deletedIsBlack = false;
	
	public int size=0;

	public RBTree() {
		nil.black = true;// property 3(Every leaf (NIL) is black) is true
		root = nil;
	}

	public RBTree(T k) throws Exception {
		this();
		root = new TreeNode<T>(k);
		root.black = true;
		root.p = nil;
		root.lchild = nil;
		root.rchild = nil;
	}

	private TreeNode<T> createTreeNode(T k) {
		try {
			TreeNode<T> res = new TreeNode<T>(k);
			res.p = nil;
			res.lchild = nil;
			res.rchild = nil;
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return nil;
		}
	}

	public TreeNode<T> getRoot() {
		return root;
	}

	/**
	 * Search the target key in this tree.
	 * 
	 * @param tgtkey
	 * @return the node
	 */
	public boolean search(T tgtkey) {
		TreeNode<T> ret = searchGetNode(tgtkey);
		return ret != nil;
	}
	
	public TreeNode<T> searchGetNode(T tgtkey) {
		TreeNode<T> now = root;
		while (now != nil) {
			if (tgtkey==now.key) {
				break;// find the answer
			} else {
				
				if(((Comparable<T>) tgtkey).compareTo(now.key) == 0){
					//traverse
					now=now.searchTraverse(tgtkey);
					if(now==null){
						now=nil;
					}
					break;
				}
				else{
					boolean toLeft = ((Comparable<T>) tgtkey).compareTo(now.key) < 0;

					if (toLeft) {
						now = now.lchild;
					} else {
						now = now.rchild;
					}
				}
				
			}
		}
		return now;
	}

	/**
	 * get the predecessor of predecessor of node now
	 */
	public TreeNode<T> predecessor(TreeNode<T> now) {
		TreeNode<T> pre = now;
		if (pre.lchild != nil) {
			pre = pre.lchild;
			while (pre.rchild != nil) {
				pre = pre.rchild;
			}
		} else {
			while (pre.p != nil && pre.p.rchild != pre) {
				pre = pre.p;
			}
			pre = pre.p;
		}
		return pre;
	}

	/**
	 * get the successor of node now
	 */
	public TreeNode<T> successor(TreeNode<T> now) {
		TreeNode<T> suc = now;
		if (suc.rchild != nil) {
			suc = suc.rchild;
			while (suc.lchild != nil) {
				suc = suc.lchild;
			}
		} else {
			while (suc.p != nil && suc.p.lchild != suc) {
				suc = suc.p;
			}
			suc = suc.p;
		}
		return suc;
	}

	/**
	 * get the smallest element of this tree
	 * 
	 * @return
	 */
	public TreeNode<T> first() {
		TreeNode<T> now = root;
		while (now.lchild != nil) {
			now = now.lchild;
		}
		return now;
	}

	public T firstValue() {
		TreeNode<T> now = root;
		while (now.lchild != nil) {
			now = now.lchild;
		}
		return now.key;
	}

	public T lastValue(){
		TreeNode<T> now = root;
		while (now.rchild != nil) {
			now = now.rchild;
		}
		return now.key;
	}
	
	public T pollFirst(){
		TreeNode<T> now = root;
		while (now.lchild != nil) {
			now = now.lchild;
		}
		delete(now.key);
		return now.key;
	}
	
	public T pollLast(){
		TreeNode<T> now = root;
		while (now.rchild != nil) {
			now = now.rchild;
		}
		delete(now.key);
		return now.key;
	}
	
	/**
	 * Walk this tree by inorder sequence
	 */
	public void inorderTreeWalk() {
		TreeNode<T> now = first();
		int k = 0;
		do {
			System.out.printf("%3d : %s\n", ++k, now.key);
			System.out.printf("  %b,%s %b,%s\n", now.lchild.black,
					now.lchild.key, now.rchild.black, now.rchild.key);
			now = successor(now);
		} while (now != nil);
	}

	/**
	 * Insert a node with given key into this tree.<br>
	 * In this method, this tree is regarded as a simple binary search tree.
	 * 
	 * @param key
	 */
	@SuppressWarnings("unchecked")
	private TreeNode<T> insertAsBST(T key) {
		TreeNode<T> node = createTreeNode(key);
		if (root == nil) {
			root = node;
			return root;
		}
		TreeNode<T> now = root;
		while (now != nil) {
			if (key==now.key)
				break;
			boolean toLeft = ((Comparable<T>) key).compareTo(now.key) <= 0;

			if (toLeft) {
				if (now.lchild != nil) {
					now = now.lchild;
				} else {
					now.lchild = node;
					node.p = now;
					return node;
				}
			} else {
				if (now.rchild != nil) {
					now = now.rchild;
				} else {
					now.rchild = node;
					node.p = now;
					return node;
				}
			}
		}
		return nil;
	}

	/**
	 * After inserting a new node in the normal way as a BST,<br>
	 * do use this methoed to fix the Red-Black Tree.
	 * 
	 * @param x
	 *            the new node that was newly inserted into this tree <br>
	 *            by insertAsBST.
	 */
	private void insertFixRBT(TreeNode<T> x) {
		while (!x.p.black) {// if x's parent is black, tree is RBT already
			if (x.p.p.lchild == x.p) {
				// x's parent is the left child of x's grandparent
				TreeNode<T> y = x.p.p.rchild;// y is the uncle of x
				if (!y.black) {
					// case 1 : x's uncle y is red
					y.switchColor();// solve case 1
					x.p.switchColor();// solve case 1
					x.p.p.switchColor();// solve case 1
					x = x.p.p;// solve case 1
				} else {
					if (x.p.rchild == x) {
						// case 2 : x's uncle y is black and x is a right child
						x = x.p;// solve case 2
						leftRotate(x);// solve case 2
						// step into case 3
					}
					// case 3 : x's uncle y is black and x is a left child
					x.p.switchColor();// solve case 3
					x.p.p.switchColor();// solve case 3
					rightRotate(x.p.p);// solve case 3
				}
			} else {
				// x's parent is the right child of x's grandparent
				TreeNode<T> y = x.p.p.lchild;
				if (!y.black) {// solve case 1
					y.switchColor();
					x.p.switchColor();
					x.p.p.switchColor();
					x = x.p.p;
				} else {
					if (x.p.lchild == x) {// solve case 2
						x = x.p;
						rightRotate(x);
					}
					// solve case 3
					x.p.switchColor();
					x.p.p.switchColor();
					leftRotate(x.p.p);
				}
			}
		}
		if (!root.black)
			root.switchColor();// make the root is black
	}

	public void insert(T key) {
		TreeNode<T> x = insertAsBST(key);
		insertFixRBT(x);
		++size;
	}

	/**
	 * delete a node with given key from this tree.<br>
	 * In this method, this tree is regarded as a simple binary search tree.
	 * 
	 * @param key
	 */
	private TreeNode<T> deleteAsBST(TreeNode<T> x) {
		TreeNode<T> ret;
		if (x.lchild != nil && x.rchild != nil) {
			TreeNode<T> y = successor(x);
			x.key = y.key;
			y.rchild.p = y.p;
			if (y.p.lchild == y)
				y.p.lchild = y.rchild;
			else
				y.p.rchild = y.rchild;
			ret = y.rchild;
			x = y;// the actually deleted node is y
		} else if (x.lchild != nil) {
			x.lchild.p = x.p;
			if (x.p.lchild == x)
				x.p.lchild = x.lchild;
			else
				x.p.rchild = x.lchild;
			if (root == x)
				root = x.lchild;
			ret = x.lchild;
		} else if (x.rchild != nil) {
			x.rchild.p = x.p;
			if (x.p.lchild == x)
				x.p.lchild = x.rchild;
			else
				x.p.rchild = x.rchild;
			if (root == x)
				root = x.rchild;
			ret = x.rchild;
		} else {
			nil.p = x.p;
			if (x.p.lchild == x)
				x.p.lchild = nil;
			else
				x.p.rchild = nil;
			if (root == x)
				root = nil;
			ret = nil;
		}
		deletedIsBlack = x.black;
		return ret;
	}

	/**
	 * After deleting a node in the normal way as a BST,<br>
	 * do use this methoed to fix the Red-Black Tree.
	 * 
	 * @param x
	 *            the node that was newly deleted from this tree <br>
	 *            by deleteAsBST.
	 */
	private void deleteFixRBT(TreeNode<T> x) {
		TreeNode<T> w;
		while (root != x && x.black) {
			if (x.p.lchild == x) {
				w = x.p.rchild;
				if (!w.black) {
					// case 1: w is red
					w.switchColor();// case 1
					x.p.switchColor();// case 1
					leftRotate(x.p);// case 1
					w = x.p.rchild;// case 1
					// after case 1 x's brother (new w) becomes red
					// step into case (2) or ((3->)4)
				}

				if (w.lchild.black && w.rchild.black) {
					// case 2: w is black and both of w's children are black
					w.switchColor();// case 2
					x = x.p;// case 2
					// this loop teminates because new x may be rchild of parent
				} else {
					if (w.rchild.black) {// in this situation w.lchild must
											// be red
						// case 3: w is black, w's left child is red and w's
						// right child is black
						w.switchColor();// case 3
						w.lchild.switchColor();// case 3
						rightRotate(w);// case 3
						w = x.p.rchild;// case 3
						// after this case, w.rchild is changed to red
						// step into case 4
					}
					// case 4: w is black and w's right child is red
					w.black = w.p.black;// case 4
					w.p.black = true;// case 4
					w.rchild.switchColor();// case 4
					leftRotate(w.p);// case 4
					x = root;// finish, jump out of the loop
				}
			} else {// the same method as above
				w = x.p.lchild;
				if (!w.black) {
					// case 1: w is red
					w.switchColor();// case 1
					x.p.switchColor();// case 1
					rightRotate(x.p);// case 1
					w = x.p.lchild;// case 1
					// after case 1 x's brother (new w) becomes red
					// step into case (2) or ((3->)4)
				}

				if (w.rchild.black && w.lchild.black) {
					// case 2: w is black and both of w's children are black
					w.switchColor();// case 2
					x = x.p;// case 2
					// this loop teminates
				} else {
					if (w.lchild.black) {// in this situation w.rchild must
											// be red
						// case 3: w is black, w's right child is red and w's
						// left child is black
						w.switchColor();// case 3
						w.rchild.switchColor();// case 3
						leftRotate(w);// case 3
						w = x.p.lchild;// case 3
						// after this case, w.lchild is changed to red
						// step into case 4
					}
					// case 4: w is black and w's left child is red
					w.black = w.p.black;// case 4
					w.p.black = true;// case 4
					w.lchild.switchColor();// case 4
					rightRotate(w.p);// case 4
					x = root;// finish, jump out of the loop
				}
			}
		}
		x.black = true;
	}

	public void delete(T key) {
		TreeNode<T> x = searchGetNode(key);
		if (x == nil)
			return;
		
		--size;
		TreeNode<T> y = deleteAsBST(x);
		if (deletedIsBlack) {
			deleteFixRBT(y);
		}
		
	}

	/**
	 * Left-Rotate on the given node
	 * 
	 * @param x
	 *            the given node
	 */
	private boolean leftRotate(TreeNode<T> x) {
		TreeNode<T> y = x.rchild;
		if (y == nil)
			return false;
		if (root == x) {
			root = y;
		} else {
			if (x.p.lchild == x)
				x.p.lchild = y;
			else
				x.p.rchild = y;
		}
		y.p = x.p;
		x.rchild = y.lchild;
		// if nil.p is changed, deleteFixRBT will fail
		if (y.lchild != nil)
			y.lchild.p = x;
		y.lchild = x;
		x.p = y;
		return true;
	}

	public int size(){
		return size;
	}
	
	/**
	 * Right-Rotate on the given node
	 * 
	 * @param x
	 *            the given node
	 */
	private boolean rightRotate(TreeNode<T> x) {
		TreeNode<T> y = x.lchild;
		if (y == nil)
			return false;
		if (root == x) {
			root = y;
		} else {
			if (x.p.lchild == x)
				x.p.lchild = y;
			else
				x.p.rchild = y;
		}
		y.p = x.p;
		x.lchild = y.rchild;
		//if nil.p is changed, deleteFixRBT will fail
		if (y.rchild != nil)
			y.rchild.p = x;
		y.rchild = x;
		x.p = y;
		return true;
	}
}
