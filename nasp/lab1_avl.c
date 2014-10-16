#include <stdio.h>
#include <malloc.h>
#include <math.h>

typedef struct tree tree;

struct tree
{
	int value;
	tree* l;
	tree* r;
};

tree* create_tree(int root_value)
{
	tree* t = (tree*) malloc(sizeof(tree));
	t->value = root_value;
	t->l = 0;
	t->r = 0;
	return t;
}

void add_node(tree* t, int value)
{
	if (value > t->value) {
		if (t->r == 0) {
			t->r = create_tree(value);
		} else {
			add_node(t->r, value);
		}
	} else {
		if (t->l == 0) {
			t->l = create_tree(value);
		} else {
			add_node(t->l, value);
		}
	}
}

tree* find_closest_l(tree* t)
{
	return t->r == 0 ? t : find_closest_l(t->r);
}

tree* get_parent(tree* cur, tree* prev, int value)
{
	if (cur == 0) {
		return 0;
	} else if (cur->value != value) {
		prev = get_parent(cur->l, cur, value);
		if (prev == 0) {
			prev = get_parent(cur->r, cur, value);
		}
	}
	return prev;
}

void rm_n(tree* prev, tree* cur, int value, tree* whole)
{	
	if (cur->value == value) {
		tree** cur_ptr;
		cur_ptr = cur->value > prev->value ? &(prev->r) : &(prev->l);
		if (cur->l == 0 && cur->r == 0) {
			// no children
			*cur_ptr = 0;
			free(cur);
		} else if (cur->l != 0 && cur->r != 0) {
			// has both children
			tree* closest = find_closest_l(cur->l);	// closest in the left subtree
			tree* sub_parent = get_parent(whole, 0, closest->value);
			cur->value = closest->value;
			if (sub_parent->value > closest->value) {
				sub_parent->l = closest->l;
			} else {
				sub_parent->r = closest->l;
			}
			free(closest);
		} else {
			// has one child
			*cur_ptr = cur->l == 0 ? cur->r : cur->l;
			free(cur);
		}
	} else {
		if (cur->l != 0) rm_n(cur, cur->l, value, whole);
		if (cur->r != 0) rm_n(cur, cur->r, value, whole);
	}
}

void remove_node(tree* t, int value)
{
	rm_n(t, t, value, t);
}

void inorder(tree* t)
{
	if (t->l != 0) inorder(t->l);
	printf("%d ", t->value);
	if (t->r != 0) inorder(t->r);
}

void preorder(tree* t)
{
	printf("v%d", t->value);
	printf("h%d ", height(t));
	if (t->l != 0) preorder(t->l);
	if (t->r != 0) preorder(t->r);
}

int height(tree* t)
{
	if (t->l == 0 && t->r == 0) {
		return 0;	// leaf
	} else if (t->l != 0 && t->r == 0) {
		return 1 + height(t->l);
	} else if (t->l == 0 && t->r != 0) {
		return 1 + height(t->r);
	} else {
		int h_l = height(t->l);
		int h_r = height(t->r);
		return 1 + (h_l > h_r ? h_l : h_r);
	}
}

int balance_factor(tree* t)
{
	return height(t->r) - height(t->l);
}

tree* rotate_left(tree* root)
{
	tree* nroot = root->r;
	root->r = root->r->l;
	nroot->l = root;
	return nroot;
}
		
tree* rotate_right(tree* root)
{
	tree* nroot = root->l;
	root->l = root->l->r;
	nroot->r = root;
	return nroot;
}

tree* rebalance(tree* avl_t, tree* parent, int value)
{
	int bf = balance_factor(avl_t);
	if (avl_t->value == value) {
		return avl_t;
	} else if (abs(bf) > 1) {
		if (bf == -2) {
			tree* p = avl_t->l;
			if (balance_factor(p) == 1) {
				avl_t->l = rotate_left(p);
			}
			// wtf...
	} else {
		return rebalance((value < avl_t->value ? avl_t->l : avl_t->r), avl_t, value);
	}
}		

tree* avl_add_node(tree* avl_t, int value)
{
	add_node(avl_t, value);
	
	
int main()
{
	tree* test = create_tree(8);
	add_node(test, 5);
	add_node(test, 2);
	add_node(test, 6);
	add_node(test, 1);
	add_node(test, 4);
	add_node(test, 3);
	add_node(test, 7);
	preorder(test);
	printf("\n");
	tree* test2 = create_tree(5);
	add_node(test2, 2);
	add_node(test2, 10);
	add_node(test2, 4);
	add_node(test2, 1);
	printf("\n");
	preorder(test2);
	printf("\n");
	inorder(test2);
	printf("\n");	
	test2 = rotate_right(test2);
	preorder(test2);
	printf("\n");
	inorder(test2);
	printf("\n");
	test2 = rotate_left(test2);
	preorder(test2);
	printf("\n");
	inorder(test2);
	printf("\n");
	/*
	remove_node(test, 3);
	remove_node(test, 4);	
	remove_node(test, 1);
	add_node(test, 1);
	add_node(test, 3);
	add_node(test, 4);
	remove_node(test, 3);
	inorder(test);
	printf("\n");
	preorder(test);
	printf("\n");
	remove_node(test, 5);
	inorder(test);
	printf("\n");
	preorder(test);
	printf("\n");
	*/
	return 0;
}

