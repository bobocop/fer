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
	
void add_node(tree** t, int value)
{
	if (!(*t)) {
		// root
		*t = create_tree(value);
	} else {
		if (value > (*t)->value) {
			if (!(*t)->r) {
				(*t)->r = create_tree(value);
			} else {
				add_node(&((*t)->r), value);
			}
		} else {
			if (!(*t)->l) {
				(*t)->l = create_tree(value);
			} else {
				add_node(&((*t)->l), value);
			}
		}
	}
}

tree* find_closest_l(tree* t)
{
	return !t->r ? t : find_closest_l(t->r);
}

tree* get_parent(tree* cur, tree* prev, int value)
{
	if (!cur || (cur == prev && cur->value == value)) {
		return 0;
	} else if (cur->value != value) {
		prev = get_parent(cur->l, cur, value);
		if (!prev) {
			prev = get_parent(cur->r, cur, value);
		}
	}
	return prev;
}

void rotate_left(tree** t, tree* whole)
{
	tree* p = get_parent(whole, whole, (*t)->value);
	printf("left rotation\n");
	p ? printf("p: %d\n", p->value) : printf("p == 0\n");
	tree* temp = (*t)->r;

	if (p) {
		(*t)->r = temp->l;
		temp->l = (*t);
		if (p->value > (*t)->value) {
			p->l = temp;
		} else {
			p->r = temp;
		}
	} else {
		temp = (*t);
		*t = (*t)->r;
		temp->r = (*t)->l;
		(*t)->l = temp;
	}
}

void rotate_right(tree** t, tree* whole)
{
	tree* p = get_parent(whole, whole, (*t)->value);
	printf("right rotation\n");
	p ? printf("p: %d\n", p->value) : printf("p == 0\n");
	tree* temp = (*t)->l;

	if (p) {
		(*t)->l = temp->r;
		temp->r = (*t);
		if (p->value > (*t)->value) {
			p->l = temp;
		} else {
			p->r = temp;
		}
	} else {
		temp = (*t);
		*t = (*t)->l;
		temp->l = (*t)->r;
		(*t)->r = temp;
	}
}	

int height(tree* t)
{
	if (!t) {
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

int balance_factor(tree* t)
{
	return height(t->r) - height(t->l);
}

void rebalance(tree** t, tree* whole) 
{
	int bf = balance_factor(*t);	
	printf("rebalancing %d, bf = %d\n", (*t)->value, bf);
	if (bf == 2) {
		if (balance_factor((*t)->r) == -1) {
			rotate_right(&((*t)->r), whole);
		}
		rotate_left(t, whole);
	} else if (bf == -2) {
		if (balance_factor((*t)->l) == 1) {
			rotate_left(&((*t)->l), whole);
		}
		rotate_right(t, whole);
	}
}	

void avl_add_node_i(tree** t, tree** whole, int value)
{
	if (!(*t)) {
		*t = create_tree(value);
	} else {
		if (value > (*t)->value) {
			if (!(*t)->r) {
				(*t)->r = create_tree(value);
			} else {
				avl_add_node_i(&((*t)->r), whole, value);
			}
		} else {
			if (!(*t)->l) {
				(*t)->l = create_tree(value);
			} else {
				avl_add_node_i(&((*t)->l), whole, value);
			}
		}
	}
	rebalance(t, *whole);
}

void avl_add_node(tree** t, int value)
{
	avl_add_node_i(t, t, value);
}

void avl_rm_n(tree* prev, tree* cur, int value, tree* whole)
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
			tree* sub_parent = get_parent(whole, whole, closest->value);
			cur->value = closest->value;
			if (sub_parent->value >= closest->value) {
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
		if (cur->l) avl_rm_n(cur, cur->l, value, whole);
		if (cur->r) avl_rm_n(cur, cur->r, value, whole);
	}
	rebalance(&prev, &whole);
}

void avl_remove_node(tree* t, int value)
{
	avl_rm_n(t, t, value, t);
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
			tree* sub_parent = get_parent(whole, whole, closest->value);
			cur->value = closest->value;
			if (sub_parent->value >= closest->value) {
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

/*
int depth(tree* t, tree* node)
{
	return t->value == node->value ? 0 : node->value < t->value ? 1 + depth(t->l, node) : 1 + depth(t->r, node);
}
*/

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
	else printf("__");
	print_space(pow(2, p) - 1);
	prev_level = level;
}

void add_zero(tree* t, int height, int depth)
{
	if (depth < height - 1) {
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



tree* copy_tree(tree* t)
{
	tree* copy;

	if (t) {
		copy = create_tree(t->value);
		copy->l = copy_tree(t->l);
		copy->r = copy_tree(t->r);
	} else return 0;

	return copy;
}			

void destroy_tree(tree* t)
{
	if (t) {
		if (t->l) destroy_tree(t->l);
		if (t->r) destroy_tree(t->r);
		free(t);
	}
}

void print_tree_bfs(tree* t)
{
	int tree_height = height(t);
	int node_n = 1;
	tree* copy = copy_tree(t);
	tree* whole = copy;
	printf("\n");
	queue* q = create_queue(pow(2, height(t) + 1) - 1);
	fill(copy);
	enqueue(q, copy);
	while (get_size(q) != 0) {
		tree* next = dequeue(q);
		print_node_bfs(next, node_n, tree_height);
		if (next->l) enqueue(q, next->l);
		if (next->r) enqueue(q, next->r);
		node_n++;
	}
	destroy_tree(copy);
	destroy_queue(q);
	printf("\n");
}	
	
int main(int argc, char* argv[])
{
	FILE* fh;
	tree* t = init_tree();
	int temp;
	int op;
	int value;

	if (argc < 2) {
		printf("Usage: %s filename\n", argv[0]);
		return 0;
	}
	
	fh = fopen(argv[1], "r");
	if (fh == 0) {
		printf("Could not open file %s\n", argv[1]);
		return -1;
	}

	fscanf(fh, " %d", &temp);
	while (!feof(fh)) {
		avl_add_node(&t, temp);
		fscanf(fh, " %d", &temp);
	}
	
	fclose(fh);
	
	fprintf(stdout, "a# - add #, r# - remove #, q - quit\n");
	
	print_tree_bfs(t);
	do {
		op = getchar();
		if (op == 'a') {
			fscanf(stdin, " %d", &value);
			avl_add_node(&t, value);
			print_tree_bfs(t);
		} else if (op == 'r') {
			fscanf(stdin, " %d", &value);
			avl_remove_node(t, value);
			print_tree_bfs(t);
		} else if (op == 'q') {
			break;
		} else {};
	} while (op != EOF);	
	
	destroy_tree(t);
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
