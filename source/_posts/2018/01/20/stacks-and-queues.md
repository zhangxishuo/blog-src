---
title: 栈和队列
date: 2018-01-20 17:15:56
categories: 学习
tags:
- data
- algorithm
---

简介

<!-- more -->

# ***栈***

## ***栈的表示与实现***

**栈**是限定仅在表尾进行插入或删除操作的线性表。表尾端称为栈顶，表头端称为栈底。

非空栈的栈顶指针始终在栈顶元素的下一个位置上。

```c
typedef struct {
  SElemType *base;
  SElemType *top;
  int stacksize;
} SqStack;
```

```c
Status InitStack(SqStack &S) {
  S.base = (SElemType *) malloc (STACK_INIT_SIZE * sizeof(SElemType));
  if (!S.base) {
    exit(OVERFLOW);                                // 空间分配失败
  }
  S.top = S.base;
  S.stacksize = STACK_INIT_SIZE;
  return OK;
}
```

```c
Status GetTop(SqStack S, SElemType &e) {
  if (S.top == S.base) {
    return ERROR;                                  // 栈空
  }
  e = *(S.top - 1);
  return OK;
}
```

```c
Status Push(SqStack &S, SElemType e) {
  if (S.top - S.base >= S.stacksize) {
    S.base = (SElemType *) realloc (S.base, (S.stacksize + STACKINCREMENT) * sizeof(SElemType));
    if (!S.base) {
      exit(OVERFLOW);                              // 空间分配失败
    }
    S.top = S.base + S.stacksize;
    S.stacksize += STACKINCREMENT;
  }
  *S.top ++ = e;
  return OK;
}
```

```c
Status Pop(SqStack &S, SElemType &e) {
  if (S.top == S.base) {
    return ERROR;                                  // 栈空
  }
  e = * -- S.top;
  return OK;
}
```

## ***栈的应用***

### ***数制转换***

```c
void conversion() {
  InitStack(S);
  scanf("%d", N);
  while (N) {
    Push(S, N % 8);
    N = N / 8;                                     // 将余数入栈
  }
  while (!StackEmpty(S)) {
    Pop(S, e);
    printf("%d", e);                               // 出栈, 打印
  }
}
```

### ***括号匹配***

出现左括号，进栈。出现右括号，检查栈是否空。若栈空，则该右括号多余。

若栈不空，和栈顶元素比较，若匹配，左括号出栈，否则不匹配。

表达式校验结束，若栈空，表示匹配，否则表示有左括号多余。

### ***行编辑程序***

```c
void LineEdit() {
  InitStack(S);
  ch = getchar();
  while (ch != EOF) {
    while (ch != EOF && ch != '\n') {
      switch (ch) {
        case '#':
          Pop(S, c);
          break;
        case '@':
          ClearStack(S);
          break;
        default:
          Push(S, ch);
          break;
      }
      ch = getchar();
    }
    将从栈底至栈顶的栈内字符传送至调用过程的数据区;
    ClearStack(S);
    if (ch != EOF) {
      ch = getchar();
    }
  }
  DestroyStack(S);
}
```

### ***迷宫求解***

若当前位置“可通”，则纳入路径，继续前进。

若当前位置“不可通”，则后退，换方向继续探索。

若四周“均无通路”，则将当前位置从路径中删除出去。

# ***队列***

和栈相反，队列是一种先进先出的线性表。允许插入的一端叫做队尾，允许删除的一端称为队头。

## ***链队列***

用带有头结点的单链表实现链队列。

```c
typedef struct QNode {
  QElemType data;
  struct QNode *next;
} QNode, * QueuePtr;
typedef struct {
  QueuePtr front;                   // 队头指针
  QueuePtr rear;                    // 队尾指针
} LinkQueue;
```

```c
Status EnQueue(LinkQueue &Q, QElemType e) {
  p = (QueuePtr) malloc (sizeof(QNode));
  if (!p) {
    exit(OVERFLOW);                 // 空间分配失败
  }
  p->data = e;
  p->next = NULL;
  Q.rear->next = p;                 // p插入队尾
  Q.rear = p;                       // rear指向p
  return OK;  
}
```

```c
Status DeQueue(LinkQueue &Q, QElemType &e) {
  if (Q.front == Q.rear) {
    return ERROR;                    // 队列为空
  }
  p = Q.front->next;
  e = p->data;
  Q.front->next = p->next;           // 移除元素
  if (Q.rear == p) {
    Q.rear = Q.front;                // 移除之后队列为空
  }
  free(q);
  return OK;
}
```

## ***循环队列***

头指针始终指向队列头元素，尾指针始终指向队列尾元素的下一个位置。

为了区别循环队列的判空与判满，规定，队列头指针在队列尾指针的下一位置是队列满的标志。

```c
#define MAXQSIZE 100                  // 最大队列长度
typedef struct {
  QElemType *base;                    // 分配的存储空间地址
  int front;                          // 头指针，指向队列头元素
  int rear;                           // 尾指针，指向队列尾元素的下一个位置
} SqQueue;
```

```c
Status EnQueue(SqQueue &Q, QElemType e) {
  if ((Q.rear + 1) % MAXQSIZE == Q.front) {
    return ERROR;                     // 队列满
  }
  Q.base[Q.rear] = e;
  Q.rear = (Q.rear + 1) % MAXQSIZE;
  return OK;
}
```

```c
Status DeQueue(SqQueue &Q, QElemType &e) {
  if (Q.front == Q.rear) {
    return ERROR;                     // 队列空
  }
  e = Q.base[Q.front];
  Q.front = (Q.front + 1) % MAXQSIZE;
  return OK;
}
```
