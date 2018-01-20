---
title: 线性表
date: 2018-01-19 17:55:25
categories: 学习
tags:
- data
- algorithm
---

简介

<!-- more -->

# ***绪论***

## ***术语***

数据结构：是相互之间存在一种或多种特定关系的数据元素的集合。

数据有下列四种基本结构：

 - **集合**
   结构中的数据元素之间除了“同属于一个集合的”的关系外，再无其他关系。
 - **线性结构**
   数据元素之间存在一个对一个的关系`@OneToOne`。
 - **树形结构**
   数据元素之间存在一个对多个的关系`@OneToMany`。
 - **图状结构**
   数据元素之间存在多个对多个的关系`@ManyToMany`。

**逻辑结构**：结构定义中的“关系”描述的是数据元素之间的逻辑关系，称为数据的“逻辑结构”。

**物理结构**：数据结构在计算机中的表示称为数据的“物理结构”。

数据元素之间的关系在计算机中有两种不同的表示方法：**顺序映像**和**非顺序映像**，并由此得到两种不同的存储结构：**顺序存储结构**和**链式存储结构**。

**顺序映像**的特点是借助元素在存储器中的相对位置来表示数据元素之间的逻辑关系。

**非顺序映像**的特点是借助指示元素存储地址的指针表示数据元素之间的逻辑关系。

**数据类型**：是一个值的集合和定义在这个值集上的一组操作的总称。

**抽象数据类型**：是指一个数学模型以及定义在该模型上的一组操作。

## ***算法分析***

**算法**：对特定问题求解步骤的一种描述。

算法设计的要求：

 - **正确性**
 - **可读性**
 - **健壮性**
 - **效率与低存储量需求**

时间复杂度与最深层循环中的原操作的执行次数成正比。语句的频度指的是该语句重复执行的次数。

时间复杂度是一个阶级，而不是一个确定的数字。

与时间复杂度类似，空间复杂度作为算法所需存储空间的量度。

# ***线性表***

一个线性表是n个数据元素的有限序列。

线性表`A`和`B`按值非递减有序排列，将`AB`归并为一个新的线性表`C`，仍按非递减排序。

```c
void MergeList(List A, List B, List &C) {
  InitList(C);                                   // 初始化C
  i = j = 1;
  k = 0;                                         // 初始化i, j, k
  a_length = ListLength(A);
  b_length = ListLength(B);                      // 获取线性表长度
  while (i <= a_length && j <= b_length) {       // AB都不为空时
    GetElem(A, i, a);
    GetElem(B, j, b);                            // 获取表中相应位置元素
    if (a <= b) {
      ListInsert(C, ++ k, a);
      ++ i;                                      // a比b小, 插入a, 下标自增
    } else {
      ListInsert(C, ++ k, b);
      ++ j;                                      // 反之, 插入b
    }
  }
  while (i <= a_length) {                        // A表有剩余元素时
    GetElem(A, i ++, a);
    ListInsert(C, ++ k, a);
  }
  while (j <= b_length) {                        // B表有剩余元素时
    GetElem(B, j ++, b);
    ListInsert(C, ++ k, b);
  }
}
```

## ***顺序表示***

线性表的顺序表示指的是用一组地址连续的存储单元依次存储线性表的数据元素。

```c
#define LIST_INIT_SIZE 100                        // 存储空间初始分配量
#define LISTINCREMENT  10                         // 存储空间分配增量
typedef struct {
  ElemType *elem;                                 // 存储空间基址
  int      length;                                // 当前长度
  int      listsize;                              // 当前分配的存储容量
} Sqlist;
```

```c
Status ListInsert_Sq(Sqlist &L, int i, ElemType e) {
  if (i < 1 || i > L.length + 1) {
    return ERROR;                                // 插入地址不合法
  }
  if (L.length >= L.listsize) {                  // 存储空间已满时
    newbase = (ElemType *) realloc (L.elem, (L.listsize + LISTINCREMENT) * sizeof(ElemType));
    if (!newbase) {
      exit(OVERFLOW);                            // 存储空间分配失败
    }
    L.elem = newbase;                            // 新内存地址
    L.listsize += LISTINCREMENT;                 // 增加存储容量
  }
}
```

## ***链式表示***

数据元素的存储映像称为“结点”，包括数据域(存储数据元素信息)与指针域(存储直接后继位置)。

此链表每个结点只包含一个指针域，故又称“单链表”或“线性链表”。

```c
typedef struct LNode {
  ElemType data;
  struct LNode *next;
} LNode, *LinkList;
```

**头指针**：整个链表的存取必须从头指针开始进行。头指针指示链表中第一个结点的存储位置。头指针为空，则表示线性表为空表。

**头结点**：在单链表的第一个结点之前附设一个结点，称为“头结点”。头结点的数据域可以存线性表的长度等信息，头结点的指针域存储指向第一个结点的指针。此时，**头指针指向头结点**。

```c
Status GetElem_L(LinkList L, int i, ElemType &e) {
  /* L为带头结点单链表的头指针 */
  p = L->next;                              // p指向第一个结点
  j = 1;                                    // j初始化为1
  while (p && j < i) {                      // 当j小于i, 且p不为空时
    p = p->next;
    j ++;                                   // 向后移动, 直至p指向第i个元素或p为空
  }
  if (!p || j > i) {
    return ERROR;                           // 第i个元素不存在
  }
  e = p->data;                              // 取第i个元素
  return OK;
}
```

```c
Status ListInsert_L(LinkList &L, int i, ElemType e) {
  /* 在带头结点的单链表中第i个位置之前插入元素e */
  p = L;
  j = 0;                                    // p指向头结点, j初始化为0
  while (p && j < i - 1) {
    p = p->next;
    j ++;                                   // 寻找第i - 1个结点
  }
  if (!p || j > i - 1) {
    return ERROR;                           // i大于表长加1或i小于1
  }
  s = (LinkList) malloc (sizeof(LNode));    // 生成结点
  s->data = e;
  s->next = p->next;
  p->next = s;                              // 插入结点
  return OK;
}
```

```c
Status ListDelete_L(LinkList &L, int i, ElemType &e) {
  /* 在带头结点的单链表中删除第i个元素，并返回数据 */
  p = L;
  j = 0;                                    // p指向头结点, j初始化为0
  while (p->next && j < i - 1) {
    p = p->next;
    j ++;                                   // 寻找第i - 1个结点
  }
  if (!(p->next) || j > i - 1) {
    return ERROR;                           // 第i个元素不存在或i小于1
  }
  q = p->next;
  p-next = q->next;                         // 删除结点
  e = q->data;
  free(q);                                  // 释放空间
  return OK;
}
```

```c
void CreateList_L(LinkList &L, int n) {
  L = (LinkList) malloc (sizeof(LNode));
  L->next = NULL;                           // 建立一个带头结点的单链表
  for (i = n; i > 0; i --) {
    p = (LinkList) malloc (sizeof(LNode));
    scanf(&p->data);
    p->next = L->next;
    L->next = p;                            // 插入到表头
  }
}
```

```c
void MergeList_L(LinkList &La, LinkList &Lb, LinkList &Lc) {
  pa = La->next;
  pb = Lb->next;
  Lc = pc = La;                              // 用La的头结点作为Lc的头结点
  while (pa && pb) {
    if (pa->data <= pb->data) {
      pc->next = pa;                         // 指向pa
      pc = pa;                               // pc指向pa
      pa = pa->next;                         // pa后移
    } else {
      pc->next = pb;
      pc = pb;
      pb = pb->next;
    }
  }
  pc->next = pa ? pa : pb;                   // 接上剩下的链表
  free(Lb);                                  // 释放Lb的头结点
}
```

循环链表是另一种形式的链式存储结构，其特点是表中最后一个结点的指针域指向头结点，整个链表形成一个环。

循环链表的操作与线性链表基本一致，差别仅在于算法中循环条件不是`p`或`p->next`是否为空，而是他们是否等于头指针。

双向链表的结点有两个指针域，其一指向直接后继，另一指向直接前驱。

```c
typedef struct DuLNode {
  ElemType data;
  struct DuLNode *prior;
  struct DuLNode *next;
} DuLNode, *DuLinkList;
```
