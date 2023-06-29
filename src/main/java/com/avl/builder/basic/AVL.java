package com.avl.builder.basic;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.avl.builder.auxiliary.Node;
import com.avl.builder.auxiliary.Pair;

/// <summary>
/// Дерево Адельсона-Вельского и Ландиса.
/// </summary>
public class AVL<TKey extends Comparable<TKey>, TValue>
{
    private static final Logger logger = LogManager.getLogger(); 

    public Node<TKey, TValue> Root;

    public AVL() {
        Root = null;
    }

    /// <summary>
    /// Количество элементов в АВЛ дереве.
    /// </summary>
    public int Count = 0;

    /// <summary>
    /// Возвращает высоту выбранного поддерева. 
    /// </summary>
    public int Height(Node<TKey, TValue> node)
    {
        if (node == null)
        {
            return 0;
        }

        return node.Height;
    }

    /// <summary>
    /// Выполняет очистку дерева от всех элементов.
    /// </summary>
    public void Clear()
    {
        Root = null;
        Count = 0;
    }

    /// <summary>
    /// Метод, возвращающий высоту всего дерева от корня.
    /// </summary>
    public int Height() {
        return Height(Root);
    }

    /// <summary>
    /// Высчитывает баланс узла как разность высот его левого и правого поддеревьев.
    /// </summary>
    public int GetBalance(Node<TKey, TValue> node)
    {
        if (node == null)
        {
            return 0;
        }

        return Height(node.Left) - Height(node.Right);
    }

    /// <summary>
    /// Добавление в дерево нового элемента.
    /// </summary>
    public void Insert(TKey key, TValue value)
    {
        if (Root == null)
        {
            Root = new Node<TKey, TValue>(key, value);
        }
        else
        {
            Node<TKey, TValue> parentNode = Find(key);
            int resultComparison = parentNode.Key.compareTo(key);

            if (resultComparison == 0)
            {
                logger.error("Duplicates!");
                return;
            }
            else if (resultComparison < 0)
            {
                parentNode.Right = new Node<TKey, TValue>(key, value, parentNode);
            }
            else
            {
                parentNode.Left = new Node<TKey, TValue>(key, value, parentNode);
            }

            parentNode.RecalculateHeight();
            BalanceTree(parentNode);
        }

        Count++;
    }

    /// <summary>
    /// Замещение узла в дереве другим узлом.
    /// </summary>
    /// <param name="replaceableNode">Узел, который необходимо заместить.</param>
    /// <param name="successorNode">Узел, который встанет на место замещенного.</param>
    public void ReplaceNodes(Node<TKey, TValue> replaceableNode, Node<TKey, TValue> successorNode)
    {
        if (successorNode == null)
        {
            if (replaceableNode == Root)
            {
                Root = null;
            }
            else if (replaceableNode.Parent.Left != null && replaceableNode.Parent.Left == replaceableNode)
            {
                replaceableNode.Parent.Left = null;
            }
            else
            {
                replaceableNode.Parent.Right = null;
            }

            return;
        }

        replaceableNode.Key = successorNode.Key;
        replaceableNode.Value = successorNode.Value;

        if (successorNode.Parent != null)
        {
            if (successorNode.Left != null)
            {
                successorNode.Left.Parent = successorNode.Parent;
                if (successorNode.Parent.Left == successorNode)
                {
                    successorNode.Parent.Left = successorNode.Left;
                }
                else
                {
                    successorNode.Parent.Right = successorNode.Left;
                }
            }
            else if (successorNode.Right != null)
            {
                successorNode.Right.Parent = successorNode.Parent;
                if (successorNode.Parent.Left == successorNode)
                {
                    successorNode.Parent.Left = successorNode.Right;
                }
                else
                {
                    successorNode.Parent.Right = successorNode.Right;
                }
            }
            else
            {
                if (successorNode.Parent.Left == successorNode)
                {
                    successorNode.Parent.Left = null;
                }
                else
                {
                    successorNode.Parent.Right = null;
                }
            }
        }
    }

    /// <summary>
    /// Удаление элемента из дерева по ключу.
    /// </summary>
    public void Remove(TKey key)
    {
        //Предложение:
        // 1. Находим узел, который необходимо удалить 
        // 2. Подбираем узел, который встанет на место удаляемого
        //    Один раз налево и до упора направо
        //    Заменяем
        // 3. Выполняем пересчет высот от родителя узла, 
        //    который был подобран для замены
        //    И пересчет от узла, который был заменен
        Node<TKey, TValue> removableNode = Find(key);

        if (removableNode == null || removableNode.Key.compareTo(key) != 0)
        {
            logger.error("Key not found!");
            return;
        }

        Node<TKey, TValue> successorNode = removableNode;
        Node<TKey, TValue> successorNodeParent = null;

        if (successorNode.Left != null)
        {
            successorNode = successorNode.Left;

            while (successorNode.Right != null)
            {
                successorNode = successorNode.Right;
            }

            successorNodeParent = successorNode.Parent;
        } else if (successorNode.Right != null)
        {
            successorNode = successorNode.Right;

            while (successorNode.Left != null)
            {
                successorNode = successorNode.Left;
            }

            successorNodeParent = successorNode.Parent;
        } else
        {
            successorNode = null;
        }

        ReplaceNodes(removableNode, successorNode);

        if (successorNodeParent != null)
            successorNodeParent.RecalculateHeight();

        Count--;
    }

    /// <summary>
    /// Поиск значения элемента в дереве по его ключу.
    /// </summary>
    /// <param name="key">Ключ для поиска.</param>
    /// <param name="value">Значение value будет задано, если такой элемент найдется.</param>
    /// <returns>True, если элемент найден и False в ином случае.</returns>
    public Pair<Boolean, TValue> TryGetValue(TKey key)
    {
        
        Node<TKey, TValue> current = Find(key);
        if (current == null || current.Key.compareTo(key) != 0)
        {
            return new Pair<Boolean, TValue>(false, null);
        }

        return new Pair<Boolean, TValue>(true, current.Value);
    }

    /// <summary>
    /// Выполняет поиск узла по ключу.
    /// </summary>
    /// <param name="key"></param>
    /// <returns>Если узел найден, то возращает узел. В противном случае родительский узел.</returns>
    public Node<TKey, TValue> Find(TKey key)
    {
        Node<TKey, TValue> current = Root;
        while (current != null)
        {
            int result = current.Key.compareTo(key);

            if (result > 0)
            {
                if (current.Left == null)
                    break;
                current = current.Left;
            }
            else if (result < 0)
            {
                if (current.Right == null)
                    break;
                current = current.Right;
            }
            else
            {
                break;
            }
        }

        return current;
    }

    /// <summary>
    /// Выполняет балансировку дереву.
    /// </summary>
    public void BalanceTree(Node<TKey, TValue> node)
    {
        //Балансировка необходима <=> когда высота левого и правого поддеревьев = 2
        //идея алгоритма балансировки: 
        //от узла вставки делаем переход к его родителям пока не вернемся в корень
        //на каждом узле в процессе обхода считаем баланс. 
        // Если баланс = 2 -> левое поддерево длинее правого -> RotateRight
        // Если баланс = -2 -> RotateLeft

        while (node != null)
        {
            int balance = GetBalance(node);

            if (balance > 1)
            {
                int leftbalance = GetBalance(node.Left);

                if (leftbalance <= 0)
                {
                    RotateLeft(node.Left);
                }

                RotateRight(node);

            }
            else if (balance < -1)
            {
                int rightbalance = GetBalance(node.Right);

                if (rightbalance >= 0)
                {
                    RotateRight(node.Right);
                }

                RotateLeft(node);
            }

            node = node.Parent;
        }
    }

    /// <summary>
    /// Осуществляет поворот АВЛ-поддерева налево.
    /// </summary>
    public void RotateLeft(Node<TKey, TValue> node)
    {
        Node<TKey, TValue> newNode = node.Right;
        node.Right = newNode.Left;
        if (newNode.Left != null)
        {
            newNode.Left.Parent = node;
        }

        newNode.Parent = node.Parent;

        if (node.Parent == null)
        {
            Root = newNode;
        }
        else if (node == node.Parent.Left)
        {
            node.Parent.Left = newNode;
        }
        else
        {
            node.Parent.Right = newNode;
        }

        newNode.Left = node;
        node.Parent = newNode;

        node.RecalculateHeight();
        newNode.RecalculateHeight();
    }

    /// <summary>
    /// Осуществляет поворот АВЛ-поддерева направо.
    /// </summary>
    public void RotateRight(Node<TKey, TValue> node)
    {
        Node<TKey, TValue> newNode = node.Left;
        node.Left = newNode.Right;
        if (newNode.Right != null)
        {
            newNode.Right.Parent = node;
        }

        newNode.Parent = node.Parent;

        if (node.Parent == null)
        { 
            Root = newNode;
        }
        else if (node == node.Parent.Right)
        { 
            node.Parent.Right = newNode;
        }
        else
        {
            node.Parent.Left = newNode;
        }

        newNode.Right = node;
        node.Parent = newNode;

        node.RecalculateHeight();
        newNode.RecalculateHeight();
    }

    /// <summary>
    /// Выполняет прямой обход дерева.
    /// </summary>
    /// <param name="node">Поддерево для обхода</param>
    public Iterable<Node<TKey, TValue>> InorderTraversal()
    {
        ArrayList<Node<TKey, TValue>> children = new ArrayList<Node<TKey, TValue>>();

        Node<TKey, TValue> currentNode, previousNode;

        if (Root == null)
        {
            return children;
        }

        currentNode = Root;
        while (currentNode != null)
        {
            if (currentNode.Left == null)
            {
                children.add(currentNode);
                currentNode = currentNode.Right;
            }
            else
            {
                previousNode = currentNode.Left;
                while (previousNode.Right != null && previousNode.Right != currentNode)
                    previousNode = previousNode.Right;

                if (previousNode.Right == null)
                {
                    previousNode.Right = currentNode;
                    currentNode = currentNode.Left;
                }
                else
                {
                    previousNode.Right = null;
                    children.add(currentNode);
                    currentNode = currentNode.Right;
                }
            } 
        } 

        return children;
    }

    /// <summary>
    /// Реализует поиск значения по индексу (ключу) и может изменять значения найденного ключа.
    /// </summary>
    /// <param name="key">Ключ для поиска</param>
    public TValue FindByKey(TKey key)
    {
        Pair<Boolean, TValue> tryGetValueResult = TryGetValue(key);
        boolean keyIsFound = tryGetValueResult.Key;

        if (!keyIsFound)
        {
            logger.error("Key not found");
            return null;
        }

        return tryGetValueResult.Value;
    }
}