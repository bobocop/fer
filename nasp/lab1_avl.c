#include <stdio.h>
#include <malloc.h>
#include <math.h>

typedef struct tree tree;

struct tree {
	int value;
	tree* l;
	tree* r;
};

void print_space(int n)
{
	int i;
	for (i = 0; i < n; i++) {
		printf("_");
	}
}

/* DISPOSABLE, CALL destroy_queue AFTER FILLING */
typedef struct
{
	int top;
	int ptr;
	tree** elements;
	int size_max;
} queue;

queue* create_queue(int size)
{
	queue* q = (queue*) malloc(sizeof(queue));
	q->ptr = 0;
	q->top = 0;
	q->elements = (tree**) malloc(sizeof(tree*) * size);
	q->size_max = size;
	return q;
}

void destroy_queue(queue* q) {
	free(q->elements);
	free(q);
}

void enqueue(queue* q, tree* element)
{
	if (q->ptr < q->size_max) {
		q->elements[q->ptr++] = element;
	/*	if (element) printf("Placed %d in the queue\n", element->value);
		else printf("Placed null\n");*/
	} else {
		fprintf(stderr, "Queue full, tried adding %d\n", element->value);
	}
}

tree* dequeue(queue* q)
{
	if (q->top < q->ptr) {
	/*	if (q->elements[q->top]) printf("Removing %d from the queue\n", q->elements[q->top]->value);
		else printf("Removed null\n");*/
		return q->elements[q->top++];
	} else {
		return 0;
	}
}

tree* peek(queue* q)
{
	return q->ptr > 0 ? q->elements[q->ptr - 1] : 0;
}

int get_size(queue* q)
{
	return q->ptr - q->top;
}

tree* init_tree()
{
	return 0;
}

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
	if (t == 0) {
		// root
		t = create_tree(value);
	}
	
	if (value > t->value) {
		if (!t->r) {
			t->r = create_tree(value);
		} else {
			add_node(t->r, value);
		}
	} else {
		if (!t->l) {
			t->l = create_tree(value);
		} else {
			add_node(t->l, value);
		}
	}
}

tree* find_closest_l(tree* t)
{
	return !t->r ? t : find_closest_l(t->r);
}

tree* get_parent(tree* cur, tree* prev, int value)
{
	if (!cur) {
		return 0;
	} else if (cur->value != value) {
		prev = get_parent(cur->l, cur, value);
		if (!prev) {
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
		if (!cur->l && !cur->r) {
			// no children
			*cur_ptr = 0;
			free(cur);
		} else if (cur->l && cur->r) {
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
			*cur_ptr = !cur->l ? cur->r : cur->l;
			free(cur);
		}
	} else {
		if (cur->l) rm_n(cur, cur->l, value, whole);
		if (cur->r) rm_n(cur, cur->r, value, whole);
	}
}

void remove_node(tree* t, int value)
{
	rm_n(t, t, value, t);
}

void inorder(tree* t)
{
	if (t->l) inorder(t->l);
	printf("%d ", t->value);
	if (t->r) inorder(t->r);
}

void preorder(tree* t)
{
	printf("v%d", t->value);
	printf("h%d ", height(t));
	if (t->l) preorder(t->l);
	if (t->r) preorder(t->r);
}

int height(tree* t)
{
	if (!t->l && !t->r) {
		return 0;	// leaf
	} else if (t->l && !t->r) {
		return 1 + height(t->l);
	} else if (!t->l && t->r) {
		return 1 + height(t->r);
	} else {
		int h_l = height(t->l);
		int h_r = height(t->r);
		return 1 + (h_l > h_r ? h_l : h_r);
	}
}
/*
int depth(tree* t, tree* node)
{
	return t->value == node->value ? 0 : node->value < t->value ? 1 + depth(t->l, node) : 1 + depth(t->r, node);
}
*/
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

void rebalance(tree* avl_t, tree* parent, int value)
{
	int bf = balance_factor(avl_t);
	if (abs(bf) > 1) {
		if (bf == -2) {
			tree* p = avl_t->l;
			if (balance_factor(p) == 1) {
				avl_t->l = rotate_right(p);
			}
			tree* nroot = rotate_left(avl_t);
			if (nroot->value < parent->value) {
				parent->l = nroot;
			} else {
				parent->r = nroot;
			}
		} else {
			tree* p = avl_t->r;
			if (balance_factor(p) == -1) {
				avl_t->r = rotate_left(p);
			}
			tree* nroot = rotate_right(avl_t);
			if (nroot->value < parent->value) {
				parent->l = nroot;
			} else {
				parent->r = nroot;
			}
		}
	} else if (avl_t->value != value) {
		rebalance((value < avl_t->value ? avl_t->l : avl_t->r), avl_t, value);
	}
	return;
}		

tree* avl_add_node(tree* avl_t, int value)
{
	add_node(avl_t, value);
}

void print_node_bfs(tree* t, int node_n, int tree_height)
{
	static int prev_level;
	int level = log2(node_n);
	int p = tree_height - level;
	if (level > prev_level) printf("\n");
	if (p > 0) {
		print_space(pow(2, p) - 1);
	}
	if (t->value) printf("%d_", t->value);
	else printf("_");
	print_space(pow(2, p) - 1);
	prev_level = level;
}

void add_zero(tree* t, int height, int depth)
{
	if (depth < height) {
		if (!t->l) {
			//printf("adding zero to %d\n", t->value);
			t->l = create_tree(0);
		}
		if (!t->r) {
			//printf("adding zero to %d\n", t->value);
			t->r = create_tree(0);
		}
		add_zero(t->l, height, depth + 1);
		add_zero(t->r, height, depth + 1);
	}
}		

void fill(tree* t)
{
	add_zero(t, height(t), 0);
}

void print_tree_bfs(tree* t)
{
	int tree_height = height(t);
	int node_n = 1;
	tree* whole = t;
	printf("\n");
	queue* q = create_queue(pow(2, height(t) + 1) - 1);
	fill(t);
	enqueue(q, t);
	while (get_size(q) != 0) {
		tree* next = dequeue(q);
		print_node_bfs(next, node_n, tree_height);
		if (next->l) enqueue(q, next->l);
		if (next->r) enqueue(q, next->r);
		node_n++;
	}
	destroy_queue(q);
	printf("\n");
}	
	
int main(int argc, char* argv[])
{
	FILE* fh;
	tree* t = init_tree();
	int temp;
	if (argc < 2) {
		printf("Usage: %s filename\n");
		return 0;
	}
	
	fh = fopen(argv[1], "r");
	if (fh == 0) {
		printf("Could not open file %s\n", argv[1]);
		return -1;
	}
	
	fscanf(fh, " %d", &temp);
	while (feof(fh)) {
		add_node(t, temp);
		fscanf(fh, " %d", &temp);
	}
	
	fclose(fh);
	
	char op;
	int value;
	fscanf(stdin, "%c%d", &op, &value);
	while (op != 'q') {
		if (op == 'a') {
			// add node
		} else if (op == 'r') {
			// remove node
		} else {
			break;
		}
		fscanf(stdin, "%c%d", &op, &value);
	}
	/*
	tree* test = create_tree(8);
	add_node(test, 5);
	add_node(test, 2);
	add_node(test, 6);
	add_node(test, 1);
	add_node(test, 4);
	add_node(test, 3);
	add_node(test, 7);
	print_tree_bfs(test);
/*	preorder(test);
	printf("\n");
	print_tree_bfs(test);
	printf("\n");
	tree* test2 = create_tree(5);
	add_node(test2, 2);
	add_node(test2, 10);
	add_node(test2, 4);
	add_node(test2, 1);
	printf("\n");
	print_tree_bfs(test2);
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

