---
title: 数据结构总结(二)
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

# ***队列***
