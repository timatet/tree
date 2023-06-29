package com.avl.builder.auxiliary;

public class Node<TKey, TValue>
    {
        public TKey Key;
        public TValue Value;

        public Node<TKey, TValue> Parent;
        public Node<TKey, TValue> Left;
        public Node<TKey, TValue> Right;

        public int Height = 1;

        public Node(TKey key, TValue value)
        {
            this.Value = value;
            this.Key = key;
            Parent = null;
        }

        public Node(TKey key, TValue value, Node<TKey, TValue> parent)
        {
            this.Value = value;
            this.Key = key;
            this.Parent = parent;
            this.Height = 1;
        }

        /// <summary>
        /// Метод пересчета высоты на которой расположен этот узел в дереве.
        /// </summary>
        public void RecalculateHeight()
        {
            Node<TKey, TValue> currentNode = this;

            while (currentNode != null)
            {
                if (currentNode.Left == null && currentNode.Right == null)
                {
                    currentNode.Height = 1;
                } else if (currentNode.Left == null)
                {
                    currentNode.Height = currentNode.Right.Height + 1;
                } else if (currentNode.Right == null)
                {
                    currentNode.Height = currentNode.Left.Height + 1;
                } else 
                {
                    int maxHeight = Math.max(currentNode.Right.Height, currentNode.Left.Height) + 1;
                    currentNode.Height = maxHeight;
                }

                currentNode = currentNode.Parent;
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object obj) {
            
            Node<TKey, TValue> otherNode = (Node<TKey, TValue>) obj;

            if (otherNode == null)
            {
                return false;
            }
            else
            {
                if (this.Key.equals(otherNode.Key) && this.Value.equals(otherNode.Value))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }

        }
    }